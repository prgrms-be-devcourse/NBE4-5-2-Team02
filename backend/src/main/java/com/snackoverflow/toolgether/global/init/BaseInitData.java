package com.snackoverflow.toolgether.global.init;

import com.snackoverflow.toolgether.domain.user.entity.Address;
import com.snackoverflow.toolgether.domain.user.entity.User;
import com.snackoverflow.toolgether.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.snackoverflow.toolgether.domain.post.entity.Category;
import com.snackoverflow.toolgether.domain.post.entity.Post;
import com.snackoverflow.toolgether.domain.post.entity.PriceType;
import com.snackoverflow.toolgether.domain.post.repository.PostRepository;
import com.snackoverflow.toolgether.domain.user.entity.Address;
import com.snackoverflow.toolgether.domain.user.entity.User;
import com.snackoverflow.toolgether.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	@Lazy
	private BaseInitData self;

	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			self.reservationInit();
		};
	}

    @Bean
    public ApplicationRunner applicationRunner2() {
        return args -> {
            self.userInit();
        };
    }

	@Transactional
	public void reservationInit() {
		if(userRepository.count() > 0) {
			return;
		}
		User user = User.builder()
			.address(new Address("서울", "강남구", "12345")) // Address 객체 생성 및 설정
			.nickname("사람")
			.password("1234")
			.score(30)
			.phoneNumber("01012345678")
			.latitude(37.5665)
			.longitude(126.9780)
			.build();
		userRepository.save(user);
		postRepository.save(Post.builder()
			.user(user)
			.title("제목입니다.")
			.content("내용입니다.")
			.category(Category.TOOL)
			.priceType(PriceType.DAY)
			.price(10000)
			.latitude(37.5665)
			.longitude(126.9780)
			.build());

		User user2 = User.builder()
			.address(new Address("부산", "해운대구", "67890"))
			.nickname("바다사람")
			.password("5678")
			.score(50)
			.phoneNumber("01098765432")
			.latitude(35.1587)
			.longitude(129.1600)
			.build();
		userRepository.save(user2);
		Post post2 = Post.builder()
			.user(user2)
			.title("부산에서 빌려드려요.")
			.content("해운대 근처에서 사용할 수 있는 드릴입니다.")
			.category(Category.TOOL)
			.priceType(PriceType.HOUR)
			.price(5000)
			.latitude(35.1587)
			.longitude(129.1600)
			.build();
		postRepository.save(post2);
	}

    @Transactional
    public void userInit() {
        System.out.println("UserInitData 실행");
        if (userRepository.count() > 0) {
            return;
        }
        try {
            // Address 객체 생성
            Address address1 = Address.builder()
                    .mainAddress("서울시 강남구")
                    .detailAddress("역삼동 123-45")
                    .zipcode("12345")
                    .build();

            // 첫 번째 사용자 생성
            User user1 = User.builder()
                    .username("testId1")
                    .password("password")
                    .nickname("닉네임1")
                    .email("test1@gmail.com")
                    .phoneNumber("000-0000-0001")
                    .address(address1)
                    .latitude(37.123)
                    .longitude(127.123)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.saveAndFlush(user1);

            // 두 번째 사용자 생성
            Address address2 = Address.builder()
                    .mainAddress("서울시 서초구")
                    .detailAddress("양재동 678-90")
                    .zipcode("67890")
                    .build();
            User user2 = User.builder()
                    .username("testId2")
                    .password("password")
                    .nickname("닉네임2")
                    .email("test2@gmail.com")
                    .phoneNumber("000-0000-0002")
                    .address(address2)
                    .latitude(37.456)
                    .longitude(127.456)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.saveAndFlush(user2);

            // 세 번째 사용자 생성
            Address address3 = Address.builder()
                    .mainAddress("서울시 종로구")
                    .detailAddress("청진동 101-11")
                    .zipcode("10111")
                    .build();
            User user3 = User.builder()
                    .username("testId3")
                    .password("password")
                    .nickname("닉네임3")
                    .email("test3@gmail.com")
                    .phoneNumber("000-0000-0003")
                    .address(address3)
                    .latitude(37.789)
                    .longitude(127.789)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.saveAndFlush(user3);

            // Google 소셜 로그인 사용자 생성
            Address googleAddress = Address.builder()
                    .mainAddress("미국 캘리포니아") // 임의의 주소
                    .detailAddress("구글 본사") // 임의의 주소
                    .zipcode("94043") // 임의의 우편번호
                    .build();
            User googleUser = User.builder()
                    .username(null) // 소셜 로그인은 username이 없을 수 있습니다.
                    .password(null) // 소셜 로그인은 password가 없습니다.
                    .nickname("구글유저")
                    .email("googleuser@gmail.com")
                    .phoneNumber("000-0000-0004") // 소셜 로그인은 전화번호가 없을 수 있습니다.
                    .address(googleAddress) // 소셜 로그인은 주소가 없을 수 있습니다.
                    .latitude(37.123) // 적절한 위도/경도 값 설정
                    .longitude(127.123)
                    .createdAt(LocalDateTime.now())
                    .provider("google") // provider 를 google 로 설정
                    .providerId("google123456789") // 실제 providerId를 설정해야 합니다.
                    .build();
            userRepository.saveAndFlush(googleUser);

            System.out.println("UserRepositoryTest 실행 완료");

        } catch (Exception e) {
            System.err.println("UserRepositoryTest 실행 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
