package com.hoainam.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hoainam.entity.Videos;
import com.hoainam.service.CategoryService;
import com.hoainam.service.UploadService;
import com.hoainam.service.VideosService;

@Controller
@RequestMapping("/admin/videos")
public class VideoController {

    @Autowired
    VideosService videoService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UploadService uploadService;

    @GetMapping("")
    public String index(Model model,
            @RequestParam(value = "keyword", required = false) String keyword) {

        List<Videos> videos = videoService.search(keyword);
        model.addAttribute("videos", videos);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "/admin/videos/list";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("newVideo", new Videos());
        model.addAttribute("categories", categoryService.findAll());
        return "/admin/videos/create";
    }

    @PostMapping("/create")
    public String createVideo(@ModelAttribute("newVideo") Videos video,
            @RequestParam("posterFile") MultipartFile file)
            throws IOException {

        try {
            if (file != null && !file.isEmpty()) {
                String fileName = uploadService.handleSaveUploadFile(file, "videos");
                video.setPoster(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        videoService.create(video);
        return "redirect:/admin/videos";
    }

    @GetMapping("/update/{id}")
    public String updatePage(Model model, @PathVariable Long id) {
        Videos video = videoService.findById(id);
        model.addAttribute("newVideo", video);
        model.addAttribute("categories", categoryService.findAll());
        return "/admin/videos/update";
    }

    @PostMapping("/update")
    public String updateVideo(
            @ModelAttribute("newVideo") Videos video,
            @RequestParam("posterFile") MultipartFile file) throws IOException {

        Videos currentVideo = videoService.findById(video.getVideoId());

        if (currentVideo != null) {

            currentVideo.setTitle(video.getTitle());
            currentVideo.setDescription(video.getDescription());
            currentVideo.setViews(video.getViews());
            currentVideo.setActive(video.getActive());

            currentVideo.setCategory(video.getCategory());

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = uploadService.handleSaveUploadFile(file, "videos");
                    currentVideo.setPoster(fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            videoService.update(currentVideo);
        }

        return "redirect:/admin/videos";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        return "/admin/videos/delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        videoService.delete(id);
        return "redirect:/admin/videos";
    }
}
