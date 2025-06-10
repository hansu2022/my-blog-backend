package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.my.blog.domain.entity.Tag;
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }
}
