package astudy.controllers;

import astudy.response.RenameWeekData;
import astudy.services.WeekService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api")
public class WeekController {
    private final WeekService weekService;

    @PutMapping("/week/rename")
    public ResponseEntity<?> renameWeek(@RequestBody RenameWeekData renameWeekData)  {
        return ResponseEntity.ok().body( weekService.renameWeek(renameWeekData));
    }
}
