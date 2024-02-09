package ua.gexlq.TelegramStudyBot.entity.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.entity.AppData;

@RequiredArgsConstructor
@Service
public class AppDataService {

    private final AppDataDAO appDataDAO;

    @PostConstruct
    @Transactional
    public void initializeAppData() {
        AppData existingData = appDataDAO.findById(1L).orElse(null);

        if (existingData == null) {
            AppData newData = AppData.builder().build();
            appDataDAO.save(newData);
        }
    }

    @Transactional
    public void updateAppData(AppData updatedData) {
        appDataDAO.save(updatedData);
    }

    @Transactional
    public AppData getAppData() {
        return appDataDAO.findById(1L).orElse(null);
    }
}
