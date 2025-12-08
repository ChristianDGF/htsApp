package com.hts.htsApp.controller;

import com.hts.htsApp.model.ContentPage;
import com.hts.htsApp.repo.ContentPageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {

    private final ContentPageRepo contentPageRepo;
    private final Logger logger = LoggerFactory.getLogger(ContentController.class);

    public ContentController(ContentPageRepo contentPageRepo) {
        this.contentPageRepo = contentPageRepo;
    }

    // Public list
    @GetMapping({"/contents", "/contents/"})
    public String listContents(Model model) {
        List<ContentPage> pages = contentPageRepo.findAll();
        model.addAttribute("pages", pages);
        return "contents/list";
    }

    // Public view by slug
    @GetMapping("/contents/{slug}")
    public String viewBySlug(@PathVariable String slug, Model model) {
        Optional<ContentPage> cp = contentPageRepo.findBySlug(slug);
        if (cp.isEmpty()) {
            return "redirect:/contents";
        }
        model.addAttribute("page", cp.get());
        return "contents/view";
    }

    // Admin list
    @GetMapping("/admin/contents")
    public String adminList(Model model) {
        List<ContentPage> pages = contentPageRepo.findAll();
        model.addAttribute("pages", pages);
        return "admin/contents-list";
    }

    @GetMapping("/admin/contents/new")
    public String newForm(Model model) {
        model.addAttribute("page", new ContentPage());
        return "admin/content-form";
    }

    @PostMapping("/admin/contents")
    public String create(@ModelAttribute ContentPage page, BindingResult br, Model model) {
        // basic validation
        if (page.getTitle() == null || page.getTitle().isBlank()) {
            model.addAttribute("error", "Title is required");
            model.addAttribute("page", page);
            return "admin/content-form";
        }

        // ensure slug exists
        String slug = page.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = page.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
            page.setSlug(slug);
        }

        // attempt to persist with retries in case of unique constraint race
        String base = page.getSlug();
        int suffix = 1;
        final int MAX_ATTEMPTS = 5;
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            try {
                // debug: log incoming payload
                logger.info("Creating ContentPage - title='{}' slug='{}' content-length={}", page.getTitle(), page.getSlug(), page.getContent() == null ? 0 : page.getContent().length());
                logger.info("Attempting to save ContentPage (slug={})", page.getSlug());
                contentPageRepo.saveAndFlush(page);
                logger.info("Saved ContentPage id={}, slug={}", page.getId(), page.getSlug());
                return "redirect:/admin/contents";
            } catch (DataIntegrityViolationException dive) {
                // likely slug unique constraint violation — compute a new slug and retry
                logger.warn("DataIntegrityViolation when saving ContentPage (attempt {}): {}", attempt + 1, dive.getMessage());
                String newSlug = base + "-" + suffix;
                page.setSlug(newSlug);
                suffix++;
            } catch (Exception ex) {
                logger.error("Unexpected error saving content page", ex);
                model.addAttribute("error", "Unexpected error saving page: " + ex.getMessage());
                model.addAttribute("page", page);
                // add debug info to the model for troubleshooting
                model.addAttribute("debug", "title='" + page.getTitle() + "', slug='" + page.getSlug() + "', content-length=" + (page.getContent() == null ? 0 : page.getContent().length()));
                return "admin/content-form";
            }
        }

        model.addAttribute("error", "Could not save page after " + MAX_ATTEMPTS + " attempts. Try a different slug.");
        model.addAttribute("page", page);
        model.addAttribute("debug", "last-tried-slug='" + page.getSlug() + "'");
        return "admin/content-form";
    }

    @GetMapping("/admin/contents/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        Optional<ContentPage> cp = contentPageRepo.findById(id);
        if (cp.isEmpty()) return "redirect:/admin/contents";
        model.addAttribute("page", cp.get());
        return "admin/content-form";
    }

    @PostMapping("/admin/contents/{id}/delete")
    public String delete(@PathVariable int id) {
        contentPageRepo.deleteById(id);
        return "redirect:/admin/contents";
    }

    @PostMapping("/admin/contents/{id}")
    public String update(@PathVariable int id, @ModelAttribute ContentPage page) {
        Optional<ContentPage> existing = contentPageRepo.findById(id);
        if (existing.isEmpty()) return "redirect:/admin/contents";
        ContentPage e = existing.get();
        e.setTitle(page.getTitle());
        e.setContent(page.getContent());
        if (page.getSlug() != null && !page.getSlug().isBlank() && !page.getSlug().equals(e.getSlug())) {
            String slug = page.getSlug().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
            String base = slug;
            int suffix = 1;
            while (contentPageRepo.existsBySlug(slug)) {
                slug = base + "-" + suffix;
                suffix++;
            }
            e.setSlug(slug);
        }
        contentPageRepo.save(e);
        return "redirect:/admin/contents";
    }
}
