package projekti;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PageMessageRepository messageRepository;

    @Autowired
    MyAccount myAccount;

    @GetMapping("/images")
    public String images(Model model) {
        model.addAttribute("images", myAccount.get().getImages());
        return "images";
    }

    @GetMapping("/images/{id}")
    public String images(Model model, @PathVariable Long id) {
        Image image = imageRepository.getOne(id);
        model.addAttribute("image", image);
        return "image";
    }

    @GetMapping(path = "/images/{id}/content")
    public ResponseEntity<byte[]> content(@PathVariable Long id) {
        Image image = imageRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.getContentLength());
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.add("Content-Disposition", "attachment; filename=" + image.getName());
        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.CREATED);
    }

    @PostMapping("/images")
    @Transactional
    public String addfile(@RequestParam MultipartFile file, @RequestParam String description) throws IOException {
        if (myAccount.get().getImages().size() < 10
                && file.getBytes().length > 0
                && file.getContentType().startsWith("image/")) {
            Image f = new Image();
            f.setContent(file.getBytes());
            f.setContentLength(file.getSize());
            f.setContentType(file.getContentType());
            f.setName(file.getOriginalFilename());
            f.setDescription(description);
            myAccount.get().getImages().add(f);
        }
        return "redirect:/images";
    }

    @DeleteMapping("/images/{id}")
    @Transactional
    public String deletefile(@PathVariable Long id) {
        myAccount.get().getImages().removeIf(o -> o.getId().equals(id));
        return "redirect:/images";
    }

    @PostMapping("/images/{id}/avatar")
    @Transactional
    public String avatar(@PathVariable Long id) {
        Optional<Image> maybeavatar = myAccount.get().getImages().stream().filter(o -> o.getId().equals(id)).findFirst();
        if (maybeavatar.isPresent()) {
            myAccount.get().setAvatar(maybeavatar.get());
        }
        return "redirect:/images";
    }
}
