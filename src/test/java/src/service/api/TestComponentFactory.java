package src.service.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;

public class TestComponentFactory {
//    public static UserDto createRandomUserDto(UserRole userRole) {
//        UserDto userDto = new UserDto();
//        userDto.setUsername(generateRandomName(30));
//        userDto.setPassword(generateRandomName(30));
//        userDto.setUserRole(userRole.name());
//
//        return userDto;
//    }
//
//    public static User createRandomUser(UserRole userRole) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        User user = new User();
//        user.setId(getRandomInt());
//        user.setUsername(generateRandomName(30));
//        user.setPassword(passwordEncoder.encode(generateRandomName(30)));
//        user.setUserRole(userRole);
//
//        return user;
//    }
//
//    public static User convertUserDtoToEntity(UserDto userDto) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        UserMapper userMapper = new UserMapper();
//
//        User user = userMapper.toEntity(userDto);
//        user.setId(getRandomInt());
//        user.setPassword(passwordEncoder.encode(generateRandomName(30)));
//
//        return user;
//    }
//
//    public static Category createRandomCategory() {
//        Category category = new Category();
//        category.setId(getRandomInt());
//        category.setName(generateRandomName(30));
//        category.setFoods(new HashSet<>());
//
//        return category;
//    }
//
//    public static Set<DeliveryZone> createRandomDeliveryZones(int size, Restaurant restaurant) {
//        Set<DeliveryZone> deliveryZones = new HashSet<>();
//        for (int i = 1; i <= size; i++) {
//            DeliveryZone deliveryZone = new DeliveryZone();
//            deliveryZone.setId(i);
//            deliveryZone.setName(generateRandomName(30));
//            deliveryZone.setRestaurants(new HashSet<>());
//            deliveryZone.addRestaurant(restaurant);
//            deliveryZones.add(deliveryZone);
//        }
//        return deliveryZones;
//    }
//
//    public static Food createRandomFood(Restaurant restaurant) {
//        Food food = new Food();
//        food.setId(getRandomInt());
//        food.setName(generateRandomName(30));
//        food.setPortion(getRandomInt());
//        food.setRestaurant(restaurant);
//        food.setCategory(createRandomCategory());
//        food.setDescription(generateRandomName(100));
//        food.setPrice(getRandomDouble());
//
//        return food;
//    }
//
//    public static Restaurant createRandomRestaurant() {
//        Restaurant restaurant = new Restaurant();
//        restaurant.setId(getRandomInt());
//        restaurant.setAdmin(createRandomUser(UserRole.ADMIN));
//        restaurant.setName(generateRandomName(30));
//        restaurant.setAddress(generateRandomName(100));
//        restaurant.setDeliveryZones(createRandomDeliveryZones(4, restaurant));
//        restaurant.setDeliveryFee(getRandomDouble());
//        restaurant.setOpeningHour(getRandomInt(23));
//        restaurant.setClosingHour(getRandomInt(23));
//        restaurant.setMenu(new HashSet<>());
//
//        return restaurant;
//    }
//
//    public static RestaurantOrder createRandomRestaurantOrder() {
//        RestaurantOrder restaurantOrder = new RestaurantOrder();
//        restaurantOrder.setId(getRandomInt());
//        restaurantOrder.setRestaurant(createRandomRestaurant());
//        restaurantOrder.setCustomer(createRandomUser(UserRole.CUSTOMER));
//        restaurantOrder.setOrderStatus(OrderStatus.PENDING);
//        restaurantOrder.setOrderNumber(generateRandomName(6));
//        restaurantOrder.setOrderNumber(generateRandomNumericalCode());
//        restaurantOrder.setDateCreated(LocalDate.now());
//        restaurantOrder.setTotalPrice(getRandomDouble());
//        restaurantOrder.setDeliveryAddress(generateRandomName(30));
//        restaurantOrder.setRemark(generateRandomName(100));
//        restaurantOrder.setWithCutlery((new Random()).nextBoolean());
//
//        return restaurantOrder;
//    }
//
//    public static List<Category> createRandomCategoriesList(int size) {
//        List<Category> categories = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            categories.add(createRandomCategory());
//        }
//        return categories;
//    }
//
//    public static List<Food> createRandomFoodsList(Restaurant restaurant, int size) {
//        List<Food> foods = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            foods.add(createRandomFood(restaurant));
//        }
//        return foods;
//    }
//
//    public static Cart createRandomCart(User user) {
//        Cart cart = new Cart();
//        cart.setId(getRandomInt());
//        cart.setCustomer(user);
//        cart.setFoods(new HashMap<>());
//
//        return cart;
//    }
}
