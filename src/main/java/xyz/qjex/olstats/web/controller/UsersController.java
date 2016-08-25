package xyz.qjex.olstats.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.plaforms.Platform;
import xyz.qjex.olstats.plaforms.Platforms;
import xyz.qjex.olstats.repos.UserListRepository;
import xyz.qjex.olstats.service.UserListService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qjex on 8/24/16.
 */
@Controller
public class UsersController {

    @Autowired
    private UserListService service;

    @Autowired
    private UserListRepository repository;

    @Autowired
    private Platforms platforms;

    @RequestMapping(value = "/lists/{id}/user/{pos}/update", method = RequestMethod.GET)
    public String updateUserForm(@PathVariable("id") String id, @PathVariable("pos") int pos, Model model) {
        UserList userList = repository.findById(id);
        model.addAttribute("postLink", "/lists/"+ id +"/user/" + pos + "/update");
        model.addAttribute("listId", id);
        User user = userList.getUsers().get(pos);
        user.setIds(apedIds(user.getIds()));
        model.addAttribute("user", user);
        return "lists/userForm";
    }

    @RequestMapping(value = "/lists/{id}/user/{pos}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") String id, @PathVariable("pos") int pos, Model model) {
        UserList userList = repository.findById(id);
        userList.getUsers().remove(pos);
        service.update(userList);
        model.addAttribute("users", userList.getUsers());
        return "lists/usersList";
    }

    @RequestMapping(value = "/lists/{id}/user/{pos}/update", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") User user,
                             @PathVariable("id") String id, @PathVariable("pos") int pos, Model model) {
        UserList userList = repository.findById(id);
        userList.getUsers().set(pos, user);
        service.update(userList);
        return "redirect:/lists/" + id + "/update";
    }

    @RequestMapping(value = "/lists/{id}/user/add", method = RequestMethod.POST)
    public String createUser(@ModelAttribute("user") User user,
                             @PathVariable("id") String id, Model model) {
        UserList userList = repository.findById(id);
        userList.addUser(user);
        service.update(userList);
        return "redirect:/lists/" + id + "/update";
    }

    @RequestMapping(value = "/lists/{id}/user/add", method = RequestMethod.GET)
    public String createUserForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("listId", id);
        model.addAttribute("postLink", "/lists/" + id + "/user/add");
        model.addAttribute("user", new User("Name", getDefaultIds()));
        return "lists/userForm";
    }

    public Map<String, String> getDefaultIds() {
        Map<String, String> ids = new HashMap<>();
        for (Platform platform : platforms.getAll()) {
            ids.put(platform.getName(), "");
        }
        return ids;
    }

    public Map<String, String> apedIds(Map<String, String> ids) {
        for (Platform platform : platforms.getAll()) {
            if (ids.containsKey(platform.getName())) continue;;
            ids.put(platform.getName(), "");
        }
        return ids;
    }
}
