package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.VeterinarianAvailabilityResponse;
import syscecilia.vet.SysCecilia.dto.VeterinarianResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;
import syscecilia.vet.SysCecilia.specification.VeterinarianSpecification;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final ConsultationRepository consultationRepository;

    private static final ZoneId BR_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final LocalTime MORNING_START = LocalTime.of(8, 0);
    private static final LocalTime MORNING_END = LocalTime.of(12, 0);
    private static final LocalTime AFTERNOON_START = LocalTime.of(13, 0);
    private static final LocalTime AFTERNOON_END = LocalTime.of(18, 0);
    private static final int MAX_INTERVALS = 10;
    private static final int MAX_DAYS_LOOKUP = 60;
    private static final String CANCELLED_STATUS = "CANCELLED";

    @Autowired
    public VeterinarianService(VeterinarianRepository veterinarianRepository,
                               ConsultationRepository consultationRepository) {
        this.veterinarianRepository = veterinarianRepository;
        this.consultationRepository = consultationRepository;
    }

    @Transactional(readOnly = true)
    public List<VeterinarianResponse> findAll(String name, Integer specialtyCode) {
        List<Veterinarian> veterinarians;

        if ((name == null || name.trim().isEmpty()) && specialtyCode == null) {
            // No filters, return all veterinarians sorted by name
            veterinarians = veterinarianRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } else {
            // Apply filters using specification
            veterinarians = veterinarianRepository.findAll(
                    VeterinarianSpecification.withFilters(name, specialtyCode),
                    Sort.by(Sort.Direction.ASC, "name")
            );
        }

        return veterinarians.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VeterinarianAvailabilityResponse> findAvailability(Long veterinarianId, LocalDate referenceDate) {
        veterinarianRepository.findById(veterinarianId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian", "id", veterinarianId));

        ZonedDateTime requestDateTime = ZonedDateTime.now(BR_ZONE);

        LocalDate startDate = referenceDate != null
                ? referenceDate
                : requestDateTime.toLocalDate();

        List<VeterinarianAvailabilityResponse> availability = new ArrayList<>();
        LocalDate currentDate = startDate;
        int inspectedDays = 0;

        while (availability.size() < MAX_INTERVALS && inspectedDays < MAX_DAYS_LOOKUP) {
            if (isBusinessDay(currentDate)) {
                List<VeterinarianAvailabilityResponse> dailyAvailability =
                        buildDailyAvailability(veterinarianId, currentDate, requestDateTime);
                for (VeterinarianAvailabilityResponse interval : dailyAvailability) {
                    availability.add(interval);
                    if (availability.size() == MAX_INTERVALS) {
                        break;
                    }
                }
            }
            currentDate = currentDate.plusDays(1);
            inspectedDays++;
        }

        return availability;
    }

    private List<VeterinarianAvailabilityResponse> buildDailyAvailability(Long veterinarianId,
                                                                         LocalDate date,
                                                                         ZonedDateTime requestDateTime) {
        LocalDateTime dayStart = date.atTime(MORNING_START);
        LocalDateTime dayEnd = date.atTime(AFTERNOON_END);

        Set<LocalTime> busyHours = consultationRepository
                .findByVeterinarianIdAndConsultationDateBetweenAndStatusNotOrderByConsultationDateAsc(
                        veterinarianId, dayStart, dayEnd, CANCELLED_STATUS)
                .stream()
                .map(Consultation::getConsultationDate)
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toCollection(TreeSet::new));

        List<VeterinarianAvailabilityResponse> intervals = new ArrayList<>();
        intervals.addAll(buildBlockAvailability(date, MORNING_START, MORNING_END, busyHours));
        intervals.addAll(buildBlockAvailability(date, AFTERNOON_START, AFTERNOON_END, busyHours));

        LocalTime earliestStart = determineEarliestStart(date, requestDateTime);
        if (earliestStart == null) {
            return intervals;
        }

        return trimIntervalsForSameDay(intervals, date, earliestStart);
    }

    private List<VeterinarianAvailabilityResponse> buildBlockAvailability(LocalDate date,
                                                                          LocalTime blockStart,
                                                                          LocalTime blockEnd,
                                                                          Set<LocalTime> busyHours) {
        List<VeterinarianAvailabilityResponse> intervals = new ArrayList<>();
        LocalTime currentStart = blockStart;
        LocalTime cursor = blockStart;

        while (cursor.isBefore(blockEnd)) {
            boolean busy = busyHours.contains(cursor);
            LocalTime next = cursor.plusHours(1);

            if (busy) {
                if (currentStart.isBefore(cursor)) {
                    intervals.add(new VeterinarianAvailabilityResponse(
                            date, currentStart, cursor, BR_ZONE.getId()));
                }
                currentStart = next;
            }

            cursor = next;
        }

        if (currentStart.isBefore(blockEnd)) {
            intervals.add(new VeterinarianAvailabilityResponse(
                    date, currentStart, blockEnd, BR_ZONE.getId()));
        }

        return intervals;
    }

    private LocalTime determineEarliestStart(LocalDate date, ZonedDateTime requestDateTime) {
        if (requestDateTime == null || !date.equals(requestDateTime.toLocalDate())) {
            return null;
        }

        LocalDateTime currentDateTime = requestDateTime.toLocalDateTime();
        LocalDateTime roundedToHour = currentDateTime.truncatedTo(ChronoUnit.HOURS);

        LocalDateTime nextSlot = currentDateTime.isEqual(roundedToHour)
                ? roundedToHour
                : roundedToHour.plusHours(1);

        if (!nextSlot.toLocalDate().isEqual(date)) {
            return AFTERNOON_END;
        }

        LocalTime nextStart = nextSlot.toLocalTime();
        if (nextStart.isAfter(AFTERNOON_END)) {
            return AFTERNOON_END;
        }

        return nextStart;
    }

    private List<VeterinarianAvailabilityResponse> trimIntervalsForSameDay(
            List<VeterinarianAvailabilityResponse> intervals,
            LocalDate date,
            LocalTime earliestStart) {

        List<VeterinarianAvailabilityResponse> adjusted = new ArrayList<>();
        for (VeterinarianAvailabilityResponse interval : intervals) {
            if (!interval.getDate().equals(date)) {
                adjusted.add(interval);
                continue;
            }

            if (!interval.getEndTime().isAfter(earliestStart)) {
                continue;
            }

            LocalTime adjustedStart = interval.getStartTime().isBefore(earliestStart)
                    ? earliestStart
                    : interval.getStartTime();

            adjusted.add(new VeterinarianAvailabilityResponse(
                    interval.getDate(),
                    adjustedStart,
                    interval.getEndTime(),
                    interval.getTimezone()
            ));
        }

        return adjusted;
    }

    private boolean isBusinessDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    private VeterinarianResponse convertToResponse(Veterinarian veterinarian) {
        String specialtyDescription = "";
        try {
            ConsultationReasonType reasonType = ConsultationReasonType.fromId(veterinarian.getSpecialtyCode());
            specialtyDescription = reasonType.getDescription();
        } catch (IllegalArgumentException e) {
            specialtyDescription = "Unknown specialty";
        }

        return new VeterinarianResponse(
                veterinarian.getId(),
                veterinarian.getName(),
                veterinarian.getSpecialtyCode(),
                specialtyDescription,
                veterinarian.getCreatedAt(),
                veterinarian.getUpdatedAt()
        );
    }
}

