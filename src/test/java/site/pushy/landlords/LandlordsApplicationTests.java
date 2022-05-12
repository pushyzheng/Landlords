//package site.pushy.landlords;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import site.pushy.landlords.core.enums.IdentityEnum;
//import site.pushy.landlords.dao.UserMapper;
//import site.pushy.landlords.pojo.DO.User;
//import site.pushy.landlords.pojo.DO.UserExample;
//import site.pushy.landlords.pojo.Player;
//import site.pushy.landlords.pojo.Room;
//import site.pushy.landlords.pojo.RoundResult;
//import site.pushy.landlords.service.AchievementService;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class LandlordsApplicationTests {
//
////	@Autowired
////	private UserMapper userMapper;
////
////	@Autowired
////	private AchievementService achievementService;
////
//////	@Test
//	public void contextLoads() {
//		User pushyzheng = getUserByName("pushyzheng");
//		User oauth = getUserByName("oauth");
//		User Pushy = getUserByName("Pushy");
//		List<User> userList = Arrays.asList(pushyzheng, oauth, Pushy);
//		List<Player> playerList = Arrays.asList(new Player(1), new Player(2), new Player(3));
//		System.out.println(userList);
//		System.out.println(playerList);
//		RoundResult result = new RoundResult(IdentityEnum.FARMER, 10);
//		result.setLandlord(1);
//
//		int index = 0;
//		int multiple = result.getMultiple();
//		for (User user : userList) {
//			boolean isWinning = false;
//			Player player = playerList.get(index);
//			// 地主获胜
//			if (result.getWinIdentity() == IdentityEnum.LANDLORD) {
//				if (player.getId() == result.getLandlord()) {
//					isWinning = true;
//					user.incrMoney(multiple * 2);
//				} else {
//					user.descMoney(multiple);
//				}
//			}
//			// 农民获胜
//			else {
//				if (player.getId() == result.getLandlord()) {
//					user.descMoney(multiple * 2);
//				} else {
//					isWinning = true;
//					user.incrMoney(multiple);
//				}
//			}
//			userMapper.updateByPrimaryKeySelective(user);
//			System.out.println(user.getUsername() + " => " +isWinning);
//			index++;
//		}
//	}
//
//	private User getUserByName(String username) {
//		UserExample userExample = new UserExample();
//		UserExample.Criteria criteria = userExample.createCriteria();
//		criteria.andUsernameEqualTo(username);
//		List<User> users = userMapper.selectByExample(userExample);
//		return users.isEmpty() ? null : users.get(0);
//	}
//}
//
