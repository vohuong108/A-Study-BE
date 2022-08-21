package com.example.astudy;

import com.example.astudy.dtos.UserDto;
import com.example.astudy.entities.*;
import com.example.astudy.repositories.UserRepo;
import com.example.astudy.services.CommonService;
import com.example.astudy.services.CourseService;
import com.example.astudy.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

import static com.example.astudy.enums.AccountStatus.*;
import static com.example.astudy.enums.AppUserRole.*;

@SpringBootApplication
@Slf4j
@EnableAsync
@EnableScheduling
public class AstudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstudyApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService,
						  CommonService commonService,
						  CourseService courseService,
						  UserRepo userRepo
	) {
		return args -> {
//			Category category1 = new Category(null, "Data Science", null);
//			Category category2 = new Category(null, "Java Spring Web", null);
//			Category category3 = new Category(null, "Mobile Develop", null);
//			Category category4 = new Category(null, "Data Engineering", null);
//			Category category5 = new Category(null, "Machine Learning", null);
//			Category category6 = new Category(null, "Agile Tutorial", null);
//			Category category7 = new Category(null, "SQL", null);
//			Category category8 = new Category(null, "Big Data", null);
//
//			commonService.saveCategory(category1);
//			commonService.saveCategory(category2);
//			commonService.saveCategory(category3);
//			commonService.saveCategory(category4);
//			commonService.saveCategory(category5);
//			commonService.saveCategory(category6);
//			commonService.saveCategory(category7);
//			commonService.saveCategory(category8);
//
//
//			userService.saveRole(new Role(null, STUDENT));
//			userService.saveRole(new Role(null, AUTHOR));
//			userService.saveRole(new Role(null, ADMIN_TRAINEE));
//			userService.saveRole(new Role(null, SUPER_ADMIN));
//
//			UserProfile userProfile = new UserProfile();
//			userProfile.setFirstName("john");
//			userProfile.setLastName("walker");
//			userProfile.setPhone("0339917862");
//			userProfile.setAddress("Thanh Xuan, Ha Noi");
//
//			userService.saveUser(new UserDto(
//					null,
//					"john_12345",
//					"12345",
//					"john@gmail.com",
//					STUDENT,
//					ACTIVE,
//					userProfile
//			));
//
//			userService.saveUser(new UserDto(
//					null,
//					"will_12345",
//					"12345",
//					"will@gmail.com",
//					AUTHOR,
//					ACTIVE,
//					null
//			));
//
//			userService.saveUser(new UserDto(
//					null,
//					"steve_12345",
//					"12345",
//					"steve@gmail.com",
//					AUTHOR,
//					ACTIVE,
//					null
//			));
//
//			userService.saveUser(new UserDto(
//					null,
//					"jim_12345",
//					"12345",
//					"jim@gmail.com",
//					ADMIN_TRAINEE,
//					ACTIVE,
//					null
//			));
//
//			userService.saveUser(new UserDto(
//					null,
//					"arnold_12345",
//					"12345",
//					"arnold@gmail.com",
//					SUPER_ADMIN,
//					ACTIVE,
//					null
//			));

//			AppUser user = userRepo.findByUsername("will_12345");
//
//			courseService.save(new Course(
//					null,
//					"Data Science Specialization",
//					"Data Framework development",
//					new Date(),
//					category1,
//					user
//			));
//			courseService.save(new Course(
//					null,
//					"Spring Boot",
//					"Web Framework development",
//					new Date(),
//					category2,
//					user
//			));
//			courseService.save(new Course(
//					null,
//					"Android programing",
//					"Mobile Framework development",
//					new Date(),
//					category3,
//					user
//			));
//			courseService.save(new Course(
//					null,
//					"Hadoop Specialization",
//					"Data Engineer course",
//					new Date(),
//					category4,
//					user
//			));

		};
	}

}
