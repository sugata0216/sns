package com.example.sns.controller;

import com.example.sns.entity.User;
import com.example.sns.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final AdminService adminService;
    private final RoleService roleService;
    private final CommentService commentService;

    public AdminController(UserService userService, PostService postService, AdminService adminService, RoleService roleService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.adminService = adminService;
        this.roleService = roleService;
        this.commentService = commentService;
    }
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("userCount", adminService.countUsers());
        model.addAttribute("postCount", adminService.countPosts());
        model.addAttribute("commentCount", adminService.countComments());
        model.addAttribute("likeCount", adminService.countLikes());
        return "admin/index";
    }
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute(
                "users",
                userService.findAll());
        return "admin/users";
    }
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(
            @PathVariable long userId) {
        userService.delete(userId);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/edit/{userId}")
    public String editUser(
            @PathVariable long userId,
            Model model) {
        model.addAttribute(
                "user",
                userService.findById(userId));
        model.addAttribute(
                "roles",
                roleService.findAll());
        return "admin/edit-user";
    }
    @PostMapping("/users/edit")
    public String updateUser(User user) {
        userService.updateRole(user);
        return "redirect:/admin/users";
    }
    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute(
                "posts",
                postService.findAll());
        return "admin/posts";
    }
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable long postId) {
        postService.deleteByAdmin(postId);
        return "redirect:/admin/posts";
    }
    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "admin/comments";
    }
}
