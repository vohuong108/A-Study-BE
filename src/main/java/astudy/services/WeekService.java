package astudy.services;

import astudy.response.RenameWeekData;
import org.springframework.stereotype.Service;

@Service
public interface WeekService {
    RenameWeekData renameWeek(RenameWeekData renameWeekData);

}
