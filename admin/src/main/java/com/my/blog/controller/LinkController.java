package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.entity.Link;
import com.my.blog.service.ICategoryService;
import com.my.blog.service.ILinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private ILinkService linkService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/getAllLink")
    @ResponseBody
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    }
    @GetMapping("/list")
    public ResponseResult listLinks(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(required = false) String name) {
        return linkService.getLinkList(pageNum, pageSize, name);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody Link link) {
        linkService.addLink(link);
        return ResponseResult.okResult("新增成功");
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id) {
        return linkService.getLinkById(id);
    }
    @PutMapping
    @ResponseBody
    public ResponseResult updateLink(@RequestBody Link link) {
        return linkService.updateLinkById(link);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable Long id) {
        linkService.deleteLinkById(id);
        return ResponseResult.okResult("操作成功");
    }
}
