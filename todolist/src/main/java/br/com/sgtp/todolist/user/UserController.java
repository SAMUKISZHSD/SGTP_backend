package br.com.sgtp.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; // Corrigido para importar RequestBody
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * MODIFICADORES:
 * public
 * private
 * protected
 */

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) { 
         //System.out.println(userModel.getUsername());
         var user = this.userRepository.findByUsername(userModel.getUsername());

         if(user != null) {
            //System.out.println("Usuário já existe");
            //mensagem de erro
            //status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
         }

        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}

