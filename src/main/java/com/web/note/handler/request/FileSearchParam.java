package com.web.note.handler.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class FileSearchParam {
    @NotNull(message = "Page number cannot be null")
    @Min(value = 1L, message = "Page must be greater than or equal to 1")
    Integer page;
    @NotNull(message = "Page size cannot be null")
    @Min(value = 10L, message = "Page size must be greater than or equal to 10")
    Integer pageSize;
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    String fileName;
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "File type can only contain alphanumeric characters and underscores")
    String fileType;
    @PastOrPresent(message = "Start time must be a past or present date")
    Date startTime;
    @PastOrPresent(message = "End time must be a past or present date")
    Date endTime;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum size must be greater than 0")
    Double minSize;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum size must be greater than 0")
    Double maxSize;
}