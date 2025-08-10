package com.ryanmosc.ApiRestEstoque.controller;

import com.ryanmosc.ApiRestEstoque.model.Exits;
import com.ryanmosc.ApiRestEstoque.service.ExitsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@Getter
@Setter
@RestController
@RequestMapping("/products/exit")
public class ExitsController {
    private  final ExitsService exitsService;

    //Create Exit
    @PostMapping
    public Exits saveExit (@RequestBody @Valid Exits exits){
        return exitsService.saveExit(exits);
    }
}
