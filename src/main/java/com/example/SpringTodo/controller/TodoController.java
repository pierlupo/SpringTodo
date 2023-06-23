package com.example.SpringTodo.controller;


import com.example.SpringTodo.entity.Todo;
import com.example.SpringTodo.service.ITodoService;
import com.example.SpringTodo.service.impl.LoginService;
import com.example.SpringTodo.service.impl.TodoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private String location = "upload-dir";

    @Autowired
    private TodoService _todoService;

    @Autowired
    private LoginService _loginService;

    @Autowired
    private HttpServletResponse _response;

    public TodoController(ITodoService _todoService) {
        this._todoService = (TodoService) _todoService;
    }

    @GetMapping("/home")
    public ModelAndView getAllProduit() {
        ModelAndView modelAndView = new ModelAndView();
        if (_todoService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("todos");
            modelAndView.addObject("todos", _todoService.findAll());
        }
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Integer id) {
        Todo t = _todoService.findById(id);
        if (t != null && _todoService.delete(t)) {
            return "redirect:/todo/home";
        }
        return "Aucune todo avec cet id";
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
        List<Todo> todos = _todoService.findAll();
        Todo todo = new Todo();
        for (Todo t : todos) {
            if (t.getId() == id) {
                todo = t;
            }
        }
        return todo;
    }

    @PostMapping("/create")
    public String postTodo(@ModelAttribute Todo todo) {

        System.out.println("todo " + todo);
        if (todo.getId() == null) {
            if (_todoService.create(todo)) {
                return "redirect:/todo/home";
            }
            return "todo/error";

        } else {
            Todo existTodo = _todoService.findById(todo.getId());
            if (existTodo != null) {
                existTodo.setTitle(todo.getTitle());
                existTodo.setCompleted(todo.isCompleted());
                existTodo.setTodoDetail(todo.getTodoDetail());
                existTodo.setPersonne(todo.getPersonne());
                existTodo.setCategories(todo.getCategories());
                if (_todoService.update(existTodo)) {
                    return "redirect:/todo/home";
                }
            }

            return "todo/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTodoForm(@PathVariable Integer id, Model model) {
        Todo to = _todoService.findById(id);
        System.out.println("to " + to);
        model.addAttribute("todo", to);


        return "formulaire";
    }

    @GetMapping("/form")
    public String afficherFormulaireCreationTodo(Model model) {
        model.addAttribute("todo", new Todo());
        return "formulaire";
    }

    @PostMapping("/update/{id}")
    public Todo updateTodo(@PathVariable("id") Integer id, @RequestBody Todo todo) {
        Todo existTodo = _todoService.findById(id);
        if (existTodo != null) {
            existTodo.setTitle(todo.getTitle());
            existTodo.setCompleted(todo.isCompleted());
            existTodo.setTodoDetail(todo.getTodoDetail());
            existTodo.setPersonne(todo.getPersonne());
            existTodo.setCategories(todo.getCategories());
            if (_todoService.update(existTodo)) {
                return existTodo;
            }
        }
        return existTodo;
    }

    @GetMapping("/formupload")
    public ModelAndView form(){
        ModelAndView vm = new ModelAndView("form-upload");
        return vm;
    }

    @GetMapping("files")
    @ResponseBody
    public List<String> getFiles() throws IOException {
        List<String> liste = new ArrayList<>();
        Files.walk(Paths.get(this.location)).forEach(path -> {
            liste.add(path.getFileName().toString());
        });
        return liste;
    }

    @PostMapping("submitForm")
    public String submitForm(@RequestParam("image") MultipartFile image) throws IOException {
        Path destinationFile = Paths.get(location).resolve(Paths.get(image.getOriginalFilename())).toAbsolutePath();
        InputStream stream = image.getInputStream();
        Files.copy(stream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return "redirect:/product/formupload";
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @PostMapping("submit")
    public ModelAndView submitLogin(@RequestParam String login, @RequestParam String password) throws IOException {
        if(_loginService.login(login, password)) {
            _response.sendRedirect("protected");
        }
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @GetMapping("protected")
    public String protectedPage() throws IOException {
        if(!_loginService.isLogged()){
            _response.sendRedirect("login");
        }
        return "redirect:/todo/home";
    }

}
