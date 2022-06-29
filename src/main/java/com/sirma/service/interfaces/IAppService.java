package com.sirma.service.interfaces;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface IAppService {
    public String uploadFile(MultipartFile file, Model model);
}
