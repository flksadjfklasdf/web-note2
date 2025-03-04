package com.web.note.handler.controller;

import com.web.note.handler.response.ResultEntity;
import com.web.note.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.web.note.constant.StatusConstant.OK;

@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {

    @RequestMapping("/getExampleContent")
    public ResultEntity<List<String>> getExampleContent() throws IOException {

        String c1 = ResourceUtil.readFileContent("res/content1.md");
        String c2 = ResourceUtil.readFileContent("res/content2.md");


        ResultEntity<List<String>> result = new ResultEntity<>();
        result.setStatus(OK);

        List<String> content = new ArrayList<>();

        content.add(c1);
        content.add(c2);

        result.setData(content);

        return result;
    }
}
