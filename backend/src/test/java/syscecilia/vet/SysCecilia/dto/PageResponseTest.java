package syscecilia.vet.SysCecilia.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageResponse DTO Tests")
class PageResponseTest {

    @Test
    @DisplayName("Should create PageResponse with no-args constructor")
    void shouldCreatePageResponseWithNoArgsConstructor() {
        // When
        PageResponse<String> pageResponse = new PageResponse<>();
        
        // Then
        assertNotNull(pageResponse);
        assertNull(pageResponse.getContent());
        assertNull(pageResponse.getPageNumber());
        assertNull(pageResponse.getPageSize());
        assertNull(pageResponse.getTotalElements());
        assertNull(pageResponse.getTotalPages());
        assertNull(pageResponse.getIsFirst());
        assertNull(pageResponse.getIsLast());
        assertNull(pageResponse.getHasNext());
        assertNull(pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should create PageResponse with all args constructor")
    void shouldCreatePageResponseWithAllArgsConstructor() {
        // Given
        List<String> content = Arrays.asList("item1", "item2", "item3");
        Integer pageNumber = 0;
        Integer pageSize = 10;
        Long totalElements = 30L;
        Integer totalPages = 3;
        Boolean isFirst = true;
        Boolean isLast = false;
        Boolean hasNext = true;
        Boolean hasPrevious = false;
        
        // When
        PageResponse<String> pageResponse = new PageResponse<>(
            content, pageNumber, pageSize, totalElements, totalPages,
            isFirst, isLast, hasNext, hasPrevious
        );
        
        // Then
        assertNotNull(pageResponse);
        assertEquals(content, pageResponse.getContent());
        assertEquals(pageNumber, pageResponse.getPageNumber());
        assertEquals(pageSize, pageResponse.getPageSize());
        assertEquals(totalElements, pageResponse.getTotalElements());
        assertEquals(totalPages, pageResponse.getTotalPages());
        assertEquals(isFirst, pageResponse.getIsFirst());
        assertEquals(isLast, pageResponse.getIsLast());
        assertEquals(hasNext, pageResponse.getHasNext());
        assertEquals(hasPrevious, pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should set and get content")
    void shouldSetAndGetContent() {
        // Given
        PageResponse<Integer> pageResponse = new PageResponse<>();
        List<Integer> content = Arrays.asList(1, 2, 3, 4, 5);
        
        // When
        pageResponse.setContent(content);
        
        // Then
        assertEquals(content, pageResponse.getContent());
    }

    @Test
    @DisplayName("Should set and get page number")
    void shouldSetAndGetPageNumber() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Integer pageNumber = 2;
        
        // When
        pageResponse.setPageNumber(pageNumber);
        
        // Then
        assertEquals(pageNumber, pageResponse.getPageNumber());
    }

    @Test
    @DisplayName("Should set and get page size")
    void shouldSetAndGetPageSize() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Integer pageSize = 20;
        
        // When
        pageResponse.setPageSize(pageSize);
        
        // Then
        assertEquals(pageSize, pageResponse.getPageSize());
    }

    @Test
    @DisplayName("Should set and get total elements")
    void shouldSetAndGetTotalElements() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Long totalElements = 150L;
        
        // When
        pageResponse.setTotalElements(totalElements);
        
        // Then
        assertEquals(totalElements, pageResponse.getTotalElements());
    }

    @Test
    @DisplayName("Should set and get total pages")
    void shouldSetAndGetTotalPages() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Integer totalPages = 8;
        
        // When
        pageResponse.setTotalPages(totalPages);
        
        // Then
        assertEquals(totalPages, pageResponse.getTotalPages());
    }

    @Test
    @DisplayName("Should set and get isFirst")
    void shouldSetAndGetIsFirst() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Boolean isFirst = true;
        
        // When
        pageResponse.setIsFirst(isFirst);
        
        // Then
        assertEquals(isFirst, pageResponse.getIsFirst());
    }

    @Test
    @DisplayName("Should set and get isLast")
    void shouldSetAndGetIsLast() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Boolean isLast = false;
        
        // When
        pageResponse.setIsLast(isLast);
        
        // Then
        assertEquals(isLast, pageResponse.getIsLast());
    }

    @Test
    @DisplayName("Should set and get hasNext")
    void shouldSetAndGetHasNext() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Boolean hasNext = true;
        
        // When
        pageResponse.setHasNext(hasNext);
        
        // Then
        assertEquals(hasNext, pageResponse.getHasNext());
    }

    @Test
    @DisplayName("Should set and get hasPrevious")
    void shouldSetAndGetHasPrevious() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        Boolean hasPrevious = false;
        
        // When
        pageResponse.setHasPrevious(hasPrevious);
        
        // Then
        assertEquals(hasPrevious, pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should handle empty content list")
    void shouldHandleEmptyContentList() {
        // Given
        List<String> emptyContent = Collections.emptyList();
        
        // When
        PageResponse<String> pageResponse = new PageResponse<>(
            emptyContent, 0, 10, 0L, 0, true, true, false, false
        );
        
        // Then
        assertNotNull(pageResponse.getContent());
        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(0L, pageResponse.getTotalElements());
        assertTrue(pageResponse.getIsFirst());
        assertTrue(pageResponse.getIsLast());
        assertFalse(pageResponse.getHasNext());
        assertFalse(pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should create last page response")
    void shouldCreateLastPageResponse() {
        // Given
        List<String> content = Arrays.asList("item1", "item2");
        
        // When
        PageResponse<String> pageResponse = new PageResponse<>(
            content, 2, 10, 22L, 3, false, true, false, true
        );
        
        // Then
        assertFalse(pageResponse.getIsFirst());
        assertTrue(pageResponse.getIsLast());
        assertFalse(pageResponse.getHasNext());
        assertTrue(pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should create middle page response")
    void shouldCreateMiddlePageResponse() {
        // Given
        List<String> content = Arrays.asList("item1", "item2", "item3");
        
        // When
        PageResponse<String> pageResponse = new PageResponse<>(
            content, 1, 10, 30L, 3, false, false, true, true
        );
        
        // Then
        assertFalse(pageResponse.getIsFirst());
        assertFalse(pageResponse.getIsLast());
        assertTrue(pageResponse.getHasNext());
        assertTrue(pageResponse.getHasPrevious());
    }

    @Test
    @DisplayName("Should work with different generic types")
    void shouldWorkWithDifferentGenericTypes() {
        // Given
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        
        // When
        PageResponse<Integer> pageResponse = new PageResponse<>(
            numbers, 0, 10, 3L, 1, true, true, false, false
        );
        
        // Then
        assertNotNull(pageResponse);
        assertEquals(3, pageResponse.getContent().size());
        assertEquals(Integer.valueOf(1), pageResponse.getContent().get(0));
    }
}

