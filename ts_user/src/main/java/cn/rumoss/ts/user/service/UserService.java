package cn.rumoss.ts.user.service;

import cn.rumoss.ts.user.dao.UserDao;
import cn.rumoss.ts.user.pojo.User;
import cn.rumoss.ts.util.IdWorker;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    //@Autowired
	//private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;// 解决Redis乱码问题

    /**
     * 解决Redis乱码问题
     * @param redisTemplate
     */
    //@Autowired(required = false)
    /*public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }*/

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     * 增加
     *
     * @param code
     * @param user
     */
    public void add(String code, User user) {
        String code_redis = (String) redisTemplate.opsForValue().get("smscode_"+user.getMobile());
        if (!StringUtils.isEmpty(code_redis)){
            if (code_redis.equals(code)){
                user.setId(idWorker.nextId()+"");
                user.setSex("0");
                user.setFanscount(0);// 关注数
                user.setFollowcount(0);// 粉丝数
                user.setOnline(0l);// 在线时长
                user.setRegdate(new Date());// 注册日期
                user.setUpdatedate(new Date());// 更新日期
                user.setLastdate(new Date());// 最后登陆日期
                user.setInterest("");
                user.setPersonality("");
                user.setAvatar("");
                user.setEmail("");
                user.setNickname("");
                // 密码加密
                String newPassWd = encoder.encode(user.getPassword());// 加密后的密码
                user.setPassword(newPassWd);
                userDao.save(user);
            }else{
                throw new RuntimeException("验证码不正确");
            }
        }else{
            throw new RuntimeException("请获取验证码");
        }
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 发送手机验证码
     * @param mobile
     */
    public void sendsms(String mobile) {

        // 1.生成6位随机数
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("准备发送的验证码是："+code);

        // 2.保存到redis中，5分钟过期
        redisTemplate.opsForValue().set("smscode_"+mobile,code,5, TimeUnit.MINUTES);

        // 3.将验证码和手机号发动到rabbitMQ中
        Map<String, String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",code);
        rabbitTemplate.convertAndSend("sms",map);

    }

    /**
     * 根据手机号和密码查询用户
     * @param mobile
     * @param password
     * @return
     */
    public User findByMobileAndPassword(String mobile,String password) {
        User user = userDao.findByMobile(mobile);
        if(null!=user  && encoder.matches(password,user.getPassword())){
            return user;
        }
        return null;
    }

    /**
     * 更新关注数
     * @param userid
     * @param x
     */
    @Transactional
    public void updateFollowcount(String userid, int x) {
        userDao.updateFollowcount(userid,x);
    }

    /**
     * 更新粉丝数
     * @param userid
     * @param x
     */
    @Transactional
    public void updateFanscount(String userid, int x) {
        userDao.updateFanscount(userid,x);
    }

}
