package com.itanoji.time.cutter.rest;

import com.itanoji.time.cutter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.itanoji.time.cutter.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam("file") MultipartFile file,
                                  @RequestParam("login") String login,
                                  @RequestParam("diagramName") String diagramName) {
        try {
            User user = userRepository.findByLogin(login);
            if(user != null) {
                Collection<String> files = user.getDiagrams();
                if(!files.contains(diagramName)) {
                    files.add(diagramName);
                    userRepository.save(user);
                }
                Path path = Paths.get(String.format("/files/%s", login)).toAbsolutePath();
                if(!Files.exists(path)) {
                   Files.createDirectories(path);
                }
                File newFile = new File(path.toFile(),diagramName+ ".json");
                FileOutputStream outputStream = new FileOutputStream(newFile);
                outputStream.write(file.getBytes());
                outputStream.close();

                return ResponseEntity.ok("File has been saved");
            } else {
                System.out.println("No user");
                return ResponseEntity.status(400).body("Bad");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Bad");
        }
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam("login") String login) {
        User user = userRepository.findByLogin(login);
        if(user != null) {
            return ResponseEntity.ok(user.getDiagrams().toArray());
        } else {
            return ResponseEntity.status(400).body("Пользователя не существует");
        }
    }

    @PostMapping("/getDiagram")
    public ResponseEntity<?> getDiagram(@RequestParam("login") String login,
                                        @RequestParam("diagramName") String diagramName) {

        File file = new File(String.format("/files/%s/%s.json", login, diagramName));
        if(file.exists()) {
            try {
                String jsonString = new String(Files.readAllBytes(file.toPath()));
                return ResponseEntity.ok(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(400).body("Ошибка при чтении файла!");
            }
        } else {
            return ResponseEntity.status(400).body("Файла не существует!");
        }
    }

}
