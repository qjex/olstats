package xyz.qjex.olstats.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.repos.UserListRepository;
import xyz.qjex.olstats.service.UserListService;

/**
 * Created by qjex on 8/23/16.
 */
@Controller
public class ListController {

    @Autowired
    private UserListService service;

    @Autowired
    private UserListRepository repository;

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public String getLists(Model model) {
        model.addAttribute("lists", repository.findAll());
        return "lists/index";
    }

    @RequestMapping(value = "/lists/create", method = RequestMethod.POST)
    public String update(@RequestParam("name") String name) {
        service.update(new UserList(name));
        return "redirect:/lists";
    }

    @RequestMapping(value = "/lists/{id}/update", method = RequestMethod.POST)
    public String updateList(@PathVariable("id") String id, @RequestParam("name") String name) {
        UserList userList = repository.findById(id);
        userList.setName(name);
        service.update(userList);
        return "redirect:/lists";
    }

    @RequestMapping(value = "/lists/{id}/update", method = RequestMethod.GET)
    public String updateListForm(@PathVariable("id") String id, Model model) {
        UserList userList = repository.findById(id);
        model.addAttribute("users", userList.getUsers());
        model.addAttribute("listId", id);
        model.addAttribute("listName", userList.getName());
        return "lists/usersList";
    }

    @RequestMapping(value = "/lists/{id}/delete", method = RequestMethod.POST)
    public String deleteList(@PathVariable("id") String id) {
        service.delete(id);
        return "redirect:/lists";
    }

    @RequestMapping(value = "/lists/add", method = RequestMethod.GET)
    public String addList() {
        return "lists/createList";
    }

}
