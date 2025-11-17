package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
    name = "PageResponse",
    description = "Generic paginated response wrapper for list endpoints"
)
public class PageResponse<T> {

    @Schema(
        description = "List of items for the current page",
        type = "array"
    )
    private List<T> content;

    @Schema(
        description = "Current page number (0-indexed)",
        example = "0"
    )
    private Integer pageNumber;

    @Schema(
        description = "Number of items per page",
        example = "20"
    )
    private Integer pageSize;

    @Schema(
        description = "Total number of items available",
        example = "150"
    )
    private Long totalElements;

    @Schema(
        description = "Total number of pages available",
        example = "8"
    )
    private Integer totalPages;

    @Schema(
        description = "Whether this is the first page",
        example = "true"
    )
    private Boolean isFirst;

    @Schema(
        description = "Whether this is the last page",
        example = "false"
    )
    private Boolean isLast;

    @Schema(
        description = "Whether there is a next page available",
        example = "true"
    )
    private Boolean hasNext;

    @Schema(
        description = "Whether there is a previous page available",
        example = "false"
    )
    private Boolean hasPrevious;

    public PageResponse() {}

    public PageResponse(List<T> content, Integer pageNumber, Integer pageSize, 
                       Long totalElements, Integer totalPages, Boolean isFirst, 
                       Boolean isLast, Boolean hasNext, Boolean hasPrevious) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}

