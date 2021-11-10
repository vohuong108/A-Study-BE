package astudy.services;

import astudy.models.Course;
import astudy.models.Week;
import astudy.repositories.CourseRepository;
import astudy.repositories.WeekRepository;
import astudy.response.RenameWeekData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WeekServiceImpl implements WeekService {
    private final WeekRepository weekRepository;
    private final CourseRepository courseRepository;

    @Override
    public RenameWeekData renameWeek(RenameWeekData renameWeekData) {
        log.info("rename data: {}", renameWeekData.toString());

        Course courseDb = courseRepository.getById(renameWeekData.getCourseId());
        List<Week> listWeek = courseDb.getWeeks();

        if(listWeek.size() != 0) {

            List<Week> listWeekFilter = listWeek.stream().filter(week -> {
                if (week.getSerialWeek() == renameWeekData.getSerialWeek()) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            log.info("filter week size in rename: {}", listWeekFilter.size());

            if (listWeekFilter.size() > 0) {
                Week weekDb = listWeekFilter.get(0);
                weekDb.setName(renameWeekData.getNewName());
                weekRepository.save(weekDb);

                return renameWeekData;
            }

        }

        Week newWeek = new Week();
        newWeek.setSerialWeek(renameWeekData.getSerialWeek());
        newWeek.setName(renameWeekData.getNewName());
        newWeek.setCourse(courseDb);

        weekRepository.save(newWeek);


        return renameWeekData;
    }
}
