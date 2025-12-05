package com.hoainam.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hoainam.entity.Category;
import com.hoainam.entity.Videos;
import com.hoainam.models.Response;
import com.hoainam.service.CategoryService;
import com.hoainam.service.UploadService;
import com.hoainam.service.VideosService;

@RestController
@RequestMapping("/api/videos")
public class VideoAPIController {

    @Autowired
    private VideosService videoService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UploadService uploadService;

    // 1. Lấy danh sách Video (Có tìm kiếm + Phân trang)
    @GetMapping
    public ResponseEntity<Response> getAllVideos(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("videoId").descending());
        Page<Videos> videoPage;

        if (keyword.isEmpty()) {
            videoPage = videoService.findAll(pageable);
        } else {
            videoPage = videoService.search(keyword, pageable);
        }

        return new ResponseEntity<>(
            new Response(true, "Lấy dữ liệu thành công", videoPage), 
            HttpStatus.OK
        );
    }

    // 2. Lấy chi tiết 1 Video (để hiển thị lên Modal sửa)
    @GetMapping("/{id}")
    public ResponseEntity<Response> getVideoById(@PathVariable Long id) {
        Videos video = videoService.findById(id);
        if (video != null) {
            return new ResponseEntity<>(new Response(true, "Thành công", video), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Không tìm thấy video", null), HttpStatus.NOT_FOUND);
    }

    // 3. Thêm mới Video
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addVideo(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("active") Boolean active,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "posterFile", required = false) MultipartFile posterFile) {

        Videos video = new Videos();
        video.setTitle(title);
        video.setDescription(description);
        video.setActive(active);
        video.setViews(0L); // Mặc định views = 0

        // Set Category
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return new ResponseEntity<>(new Response(false, "Category không tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        video.setCategory(category);

        // Upload Poster
        if (posterFile != null && !posterFile.isEmpty()) {
            String savedFilename = uploadService.handleSaveUploadFile(posterFile, "videos");
            video.setPoster(savedFilename);
        }

        videoService.create(video);
        return new ResponseEntity<>(new Response(true, "Thêm video thành công", video), HttpStatus.OK);
    }

    // 4. Cập nhật Video
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateVideo(
            @RequestParam("videoId") Long videoId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("active") Boolean active,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "posterFile", required = false) MultipartFile posterFile) {

        Videos currentVideo = videoService.findById(videoId);
        if (currentVideo == null) {
            return new ResponseEntity<>(new Response(false, "Video không tồn tại", null), HttpStatus.NOT_FOUND);
        }

        currentVideo.setTitle(title);
        currentVideo.setDescription(description);
        currentVideo.setActive(active);

        // Update Category
        Category category = categoryService.findById(categoryId);
        if (category != null) {
            currentVideo.setCategory(category);
        }

        // Update Poster nếu có file mới
        if (posterFile != null && !posterFile.isEmpty()) {
            String savedFilename = uploadService.handleSaveUploadFile(posterFile, "videos");
            currentVideo.setPoster(savedFilename);
        }

        videoService.update(currentVideo);
        return new ResponseEntity<>(new Response(true, "Cập nhật thành công", currentVideo), HttpStatus.OK);
    }

    // 5. Xóa Video
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteVideo(@PathVariable Long id) {
        if (videoService.findById(id) == null) {
            return new ResponseEntity<>(new Response(false, "Video không tồn tại", null), HttpStatus.NOT_FOUND);
        }
        videoService.delete(id);
        return new ResponseEntity<>(new Response(true, "Xóa thành công", null), HttpStatus.OK);
    }
}