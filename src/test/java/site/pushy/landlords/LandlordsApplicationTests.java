package site.pushy.landlords;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;

import javax.annotation.Resource;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LandlordsApplicationTests {

	@Resource
	private UserMapper userMapper;

	@Test
	public void contextLoads() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setUsername("Pushy");

		userMapper.insert(user);
	}

}

