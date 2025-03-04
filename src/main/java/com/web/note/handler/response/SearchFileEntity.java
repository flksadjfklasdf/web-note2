package com.web.note.handler.response;

import com.web.note.entity.FileResource;
import lombok.Data;

import java.util.List;

@Data
public class SearchFileEntity {
    public List<FileResourceVO> files;
    public Integer totalPages;
}
