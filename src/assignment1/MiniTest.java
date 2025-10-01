package assignment1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.util.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MiniTest {

    private  String[] getPathFieldsNames() {
        String[] names = new String[3]; // onPath, towH, towN
        Tile towH = new Tile();
        Tile towN = new Tile();
        Tile sample = new Tile(0, false, false, true, towH, towN, null, null);
        try {
            Field[] declaredFields = Tile.class.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getType() == boolean.class) {
                    f.setAccessible(true);
                    boolean bValue = (boolean) f.get(sample);
                    if (bValue == true) {
                        names[0] = f.getName();
                    }
                } else if (f.getType() == Tile.class) {
                    f.setAccessible(true);
                    Tile t = (Tile) f.get(sample);
                    if (t == towH)
                        names[1] = f.getName();
                    else if (t == towN)
                        names[2] = f.getName();
                }
            }
            return names;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return names;
    }


    private  String getNestFieldName() {
        String name = new String();
        Tile towH = new Tile();
        Tile towN = new Tile();
        Tile sample = new Tile(0, false, false, true, towH, towN, null, null);
        try {
            Field[] declaredFields = Tile.class.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getType() == boolean.class) {
                    f.setAccessible(true);
                    boolean bValue = (boolean) f.get(sample);
                    if (bValue == true) {
                        name = f.getName();
                    }
                }
            }
            return name;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return name;
    }


    private  String getHiveFieldName() {
        String name = new String();
        Tile towH = new Tile();
        Tile towN = new Tile();
        Tile sample = new Tile(0, true, false, false, towH, towN, null, null);
        try {
            Field[] declaredFields = Tile.class.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getType() == boolean.class) {
                    f.setAccessible(true);
                    boolean bValue = (boolean) f.get(sample);
                    if (bValue == true) {
                        name = f.getName();
                    }
                }
            }

            return name;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return name;
    }

    private  String[] getQueenFieldsNames() {
        String[] names = new String[2]; // isQueen, numOfQueens
        Tile towH = new Tile();
        Tile towN = new Tile();
        Tile sample = new Tile(0, false, false, true, towH, towN, null, new SwarmOfHornets());
        Hornet sampleHornet = new Hornet(sample, 10, 20);
        try {
            Field[] declaredFields = Hornet.class.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getType() == boolean.class) {
                    f.setAccessible(true);
                    boolean bValue = (boolean) f.get(sampleHornet);
                    if (bValue == false) {
                        names[0] = f.getName();
                    }
                } else if (f.getType() == int.class) {
                    f.setAccessible(true);
                    int intValue = (int) f.get(sampleHornet);
                    if (intValue == 0 || intValue == 1)
                        names[1] = f.getName();
                }
            }
            return names;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return names;
    }

    private void assertField(Class<?> clazz, String name, Class<?> type, boolean shouldBePublic, boolean shouldBe, boolean shouldBePrivate) {
        try {
            Field f = clazz.getDeclaredField(name);
            assertEquals(type, f.getType(), clazz.getSimpleName() + "." + name + " must be of type " + type.getSimpleName());
            int mods = f.getModifiers();
            if (shouldBePublic) {
                assertTrue(Modifier.isPublic(mods), clazz.getSimpleName() + "." + name + " must be public");
            }
            if (shouldBePrivate) {
                assertTrue(Modifier.isPrivate(mods), clazz.getSimpleName() + "." + name + " must be private");
            }
            if (shouldBe) {
                assertTrue(Modifier.isStatic(mods), clazz.getSimpleName() + "." + name + " must be static");
            }
        } catch (NoSuchFieldException e) {
            fail("Missing field: " + clazz.getSimpleName() + "." + name);
        }
    }

    private  boolean isSubclass(Class<?> childClass, Class<?> parentClass) {
        if (!parentClass.isAssignableFrom(childClass)) {
            System.out.println(childClass.getSimpleName() + " should be a subclass of " + parentClass.getSimpleName());
            return false;
        }
        return true;

    }

    private boolean testMethodNamesForClass(String className, String... expectedMethodNames) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("assignment1." + className);
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> methodMap = new HashMap<>();
        boolean allMethodsExist = true;
        // Add all declared methods to a map for quick lookup
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }

        // Check if each expected method name exists and print those that are missing
        for (String methodName : expectedMethodNames) {
            if (!methodMap.containsKey(methodName)) {
                System.out.println("Missing expected method name in class " + className + ": " + methodName);
                allMethodsExist = false;
            }
        }
        return allMethodsExist;
    }

    public  boolean checkFieldsForClass(String className, Class<?>... expectedFieldTypes) {
        boolean classFieldsExist = true;
        try {
            Class<?> clazz = Class.forName("assignment1." + className);
            Field[] fields = clazz.getDeclaredFields();

            Set<Class<?>> expectedTypesSet = new HashSet<>();
            for (Class<?> expectedFieldType : expectedFieldTypes) {
                expectedTypesSet.add(expectedFieldType);
            }

            for (Field field : fields) {
                if (expectedTypesSet.contains(field.getType())) {
                    expectedTypesSet.remove(field.getType());
                }
            }
            if (!expectedTypesSet.isEmpty()) {
                System.out.println("Missing expected declared field(s) in class " + className + ": " + expectedTypesSet);
                classFieldsExist = false;
            }
        } catch (ClassNotFoundException e) {
            // Handle class not found exception here if needed
            return false;
        }
        return classFieldsExist;
    }


    @Test
    @DisplayName("Test if the expected class names exist")
    @Tag("score:0")
    void testClassNames() {
        String[] expectedClassNames = {"AngryBee", "BusyBee", "FireBee", "HoneyBee", "Hornet", "Insect", "SniperBee", "SwarmOfHornets", "Tile"};
        boolean allClassesExist = true;
        for (String className : expectedClassNames) {
            try {
                Class.forName("assignment1." + className);
            } catch (ClassNotFoundException e) {
                System.out.println("Missing class: " + className);
                allClassesExist = false;
            }
        }
        // Print that all classes exist
        assertTrue(allClassesExist, "Not all expected classes exist..");
        System.out.println("All expected class names exist..");

    }


    //    @Test
    @Tag("score:0")
    @Tag("P")
    @Tag("token:abstract-insect-1")
    @DisplayName("Test if Insect and HoneyBee are abstract classes")
    void testAbstractClasses() {
        // Test if Unit and MilitaryUnit are abstract
        assertTrue(Modifier.isAbstract(HoneyBee.class.getModifiers()), "HoneyBee should be an abstract class..");
        assertTrue(Modifier.isAbstract(Insect.class.getModifiers()), "Insect should be an abstract class..");
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:abstract-insect-1")
    @DisplayName("Test that Insect Class has abstract method takeAction")
    void testAbstractTakeAction() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                // Test that Insect Class has abstract method takeAction
                boolean hasAbstractMethodTakeAction = false;
                for (Method method : Insect.class.getDeclaredMethods()) {
                    if (method.getName().equals("takeAction") && Modifier.isAbstract(method.getModifiers())) {
                        hasAbstractMethodTakeAction = true;
                        break;
                    }
                }
                assertTrue(hasAbstractMethodTakeAction, "Insect should have an abstract method takeAction..");
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:0")
    @Tag("P")
    @Tag("token:inheritance-1")
    @DisplayName("Testing inheritance for Insect and HoneyBee")
    void testInheritance() {
        boolean allInheritanceCorrect = true;
        allInheritanceCorrect &= isSubclass(AngryBee.class, HoneyBee.class);
        allInheritanceCorrect &= isSubclass(BusyBee.class, HoneyBee.class);
        allInheritanceCorrect &= isSubclass(FireBee.class, HoneyBee.class);
        allInheritanceCorrect &= isSubclass(SniperBee.class, HoneyBee.class);
        allInheritanceCorrect &= isSubclass(HoneyBee.class, Insect.class);
        allInheritanceCorrect &= isSubclass(Hornet.class, Insect.class);

        assertTrue(allInheritanceCorrect, "Not all inheritances are correct..");
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-insect-hornet-1")
    @DisplayName("Test Insect fields")
    void testInsectFields(){
        {
            assertField(Insect.class, "position", Tile.class, false, false, true);
            assertField(Insect.class, "health", int.class, false, false, true);
        }
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-insect-hornet-1")
    @DisplayName("Test Hornet fields")
    void testHornetFields() {
        // instance (per-object) fields
        assertField(Hornet.class, "attackDamage", int.class, false, false, true);
        assertField(Hornet.class, "isTheQueen", boolean.class, false, false, true);
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-insect-hornet-1")
    @DisplayName("Test Hornet  fields")
    void testHornetFields2() {
        // class-wide () fields
        assertField(Hornet.class, "numOfQueens", int.class, false, false, true); // private
        assertField(Hornet.class, "BASE_FIRE_DMG", int.class, true, true, false); // public
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test HoneyBee fields")
    void testHoneyBeeFields() {
        {
            assertField(HoneyBee.class, "cost", int.class, false, false, true);
        }
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test BusyBee fields")
    void testBusyBeeFields() {
        {
            assertField(BusyBee.class, "BASE_HEALTH", int.class, true, true, false);
            assertField(BusyBee.class, "BASE_COST", int.class, true, true, false);
        }
    }

    @Test
    @Tag("P")
    @Tag("token:fields-bees-1")
    @Tag("score:1")
    @DisplayName("Test BusyBee field AMOUNT COLLECTED")
    void testBusyBeeAmountCollected()  {
        // check that BASE AMOUNT COLLECTED is
        Field baseAmountCollected = null;
        try {
            baseAmountCollected = BusyBee.class.getDeclaredField("BASE_AMOUNT_COLLECTED");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Modifier.isPublic(baseAmountCollected.getModifiers()));
        assertTrue(Modifier.isStatic(baseAmountCollected.getModifiers()));
        assertEquals(int.class, baseAmountCollected.getType());
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test AngryBee fields")
    void testAngryBeeFields() {
        {
            assertField(AngryBee.class, "attackDamage", int.class, false, false, true);
            assertField(AngryBee.class, "BASE_HEALTH", int.class, true, true, false);
            assertField(AngryBee.class, "BASE_COST", int.class, true, true, false);
        }
    }


    @Test
    @Tag("P")
    @Tag("token:fields-bees-1")
    @Tag("score:1")
    @DisplayName("Test HoneyBee field HIVE DAMAGE REDUCTION")
    void testHoneyBeeFields2() {
        {
            assertField(HoneyBee.class, "HIVE_DMG_REDUCTION", double.class, true, true, false);
        }
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test HoneyBee field HIVE DAMAGE REDUCTION")
    void testHoneyBeeFieldsDamage() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                // check that HIVE_DMG_REDUCTION is
                try {
                    Field hiveDmgReductionField = HoneyBee.class.getDeclaredField("HIVE_DMG_REDUCTION");
                    assertTrue(Modifier.isPublic(hiveDmgReductionField.getModifiers()));
                    assertTrue(Modifier.isStatic(hiveDmgReductionField.getModifiers()));
                    assertEquals(double.class, hiveDmgReductionField.getType());
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test BusyBee field AMOUNT COLLECTED")
    void testBusyBeeFields2() {
        {
            assertField(BusyBee.class, "BASE_AMOUNT_COLLECTED", int.class, true, true, false);
        }
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test FireBee fields")
    void testFireBeeFields() {
        {
            assertField(FireBee.class, "BASE_HEALTH", int.class, true, true, false);
            assertField(FireBee.class, "BASE_COST", int.class, true, true, false);
            assertField(FireBee.class, "attackRange", int.class, false, false, true);
        }
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-bees-1")
    @DisplayName("Test SniperBee fields")
    void testSniperBeeFields() {
        {
            assertField(SniperBee.class, "BASE_HEALTH", int.class, true, true, false);
            assertField(SniperBee.class, "BASE_COST", int.class, true, true, false);
            assertField(SniperBee.class, "attackDamage", int.class, false, false, true);
            assertField(SniperBee.class, "piercingPower", int.class, false, false, true);
        }
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-swarm-1")
    @DisplayName("Test SwarmOfHornets fields")
    void testSwarmOfHornetsFields() {
        {
            assertField(SwarmOfHornets.class, "hornets", Hornet[].class, false, false, true);
            assertField(SwarmOfHornets.class, "numOfHornets", int.class, false, false, true);
            assertField(SwarmOfHornets.class, "QUEEN_BOOST", double.class, true, true, false);
        }
    }



    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:fields-tile-1")
    @DisplayName("Test Tile fields")
    void testTileFields() {
        {
            assertField(Tile.class, "food", int.class, false, false, true);
            assertField(Tile.class, "isHive", boolean.class, false, false, true);
            assertField(Tile.class, "isNest", boolean.class, false, false, true);
            assertField(Tile.class, "onThePath", boolean.class, false, false, true);
            assertField(Tile.class, "towardTheHive", Tile.class, false, false, true);
            assertField(Tile.class, "towardTheNest", Tile.class, false, false, true);
            assertField(Tile.class, "bee", HoneyBee.class, false, false, true);
            assertField(Tile.class, "swarm", SwarmOfHornets.class, false, false, true);
            // Fire-bee extension
            assertField(Tile.class, "onFire", boolean.class, false, false, true);
        }
    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-hornet-1")
    
    @DisplayName("Test Hornet constructor initializing fields of super class")
    void testHornetConstructor1() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                try {
                    String[] fieldNames = getPathFieldsNames();
                    String onPath = fieldNames[0];
                    Tile t = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(t, true);
                    Insect hornet = new Hornet(t, 10, 2);

                    assertEquals(t, hornet.getPosition(), "Position should be initialized to the input tile");
                    assertEquals(10, hornet.getHealth(), "Health was not initialized correctly");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }


    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-hornet-1")
    
    @DisplayName("Test Hornet constructor initializing fields of current class")
    void testHornetConstructor2() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                try {
                    String[] fieldNames = getPathFieldsNames();
                    String onPath = fieldNames[0];
                    Tile t = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(t, true);
                    Insect hornet = new Hornet(t, 10, 20);

                    boolean attackInitializedCorrectly = false;
                    Field[] declaredFields = Hornet.class.getDeclaredFields();
                    for (Field field : declaredFields) {
                        if (field.getType() == int.class) {
                            field.setAccessible(true);
                            int intValue = (int) field.get(hornet);
                            if (intValue == 20) {
                                attackInitializedCorrectly = true;
                                break;
                            }
                        }
                    }
                    assertTrue(attackInitializedCorrectly, "Attack damage was not initialized correctly");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-bees-1")
    
    @DisplayName("Test BusyBee constructor calls super constructor")
    void testBusyBeeConstructor1() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // Test that the constructor calls the super constructor 1pt
                Tile t = new Tile();
                BusyBee.BASE_COST = 2;
                BusyBee.BASE_HEALTH = 5;
                BusyBee bee = new BusyBee(t);
                assertEquals(t, bee.getPosition(), "Position should be initialized to the input tile");
                assertEquals(5, bee.getHealth(), "Health was not initialized correctly");
                assertEquals(2, bee.getCost(), "Cost was not initialized correctly");

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-bees-1")
    
    @DisplayName("Test AngryBee constructor initializing attack damage")
    void testAngryBeeConstructor1() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                try {
                    Tile t = new Tile();
                    AngryBee angryBee = new AngryBee(t, 20);
                    boolean attackInitializedCorrectly = false;
                    Field[] declaredFields = AngryBee.class.getDeclaredFields();
                    for (Field field : declaredFields) {
                        if (field.getType() == int.class) {
                            field.setAccessible(true);
                            int intValue = (int) field.get(angryBee);
                            if (intValue == 20) {
                                attackInitializedCorrectly = true;
                                break;
                            }
                        }
                    }
                    assertTrue(attackInitializedCorrectly, "AngryBee attack damage was not initialized correctly");


                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-fire-sniper-1")
    
    @DisplayName("Test constructor")
    void testConstructorFireBee() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                try {
                    Tile aTile = new Tile();
                    FireBee testFireBee = new FireBee(aTile, 14);

                    // Dynamically find the maxRange field
                    Field[] declaredFields = FireBee.class.getDeclaredFields();
                    boolean maxRangeInitializedCorrectly = false;

                    for (Field field : declaredFields) {
                        if (field.getType() == int.class) {
                            field.setAccessible(true);
                            int intValue = (int) field.get(testFireBee);
                            if (intValue == 14) {
                                maxRangeInitializedCorrectly = true;
                                break;
                            }
                        }
                    }

                    assertTrue(maxRangeInitializedCorrectly, "maxRange field not found or initialized correctly");
                    assertEquals(aTile, testFireBee.getPosition(), "Constructor did not instantiate the field position correctly");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }
    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:ctor-fire-sniper-1")
    
    @DisplayName("Test constructor")
    void testConstructorSniperBee() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                try {
                    Tile aTile = new Tile();
                    SniperBee testSniperBee = new SniperBee(aTile, 12, 11);
                    Field[] declaredFields = SniperBee.class.getDeclaredFields();
                    boolean attackDamageInitializedCorrectly = false;
                    boolean piercingPowerInitializedCorrectly = false;

                    for (Field field : declaredFields) {
                        if (field.getType() == int.class) {
                            field.setAccessible(true);
                            int intValue = (int) field.get(testSniperBee);
                            if (intValue == 12) {
                                piercingPowerInitializedCorrectly = true;
                                break;
                            }
                        }
                    }

                    for (Field field : declaredFields) {
                        if (field.getType() == int.class) {
                            field.setAccessible(true);
                            int intValue = (int) field.get(testSniperBee);
                            if (intValue == 11) {
                                attackDamageInitializedCorrectly = true;
                                break;
                            }
                        }
                    }

                    assertTrue(attackDamageInitializedCorrectly, "attack damage field not found or initialized correctly");
                    assertTrue(piercingPowerInitializedCorrectly, "piercing power field not found or initialized correctly");
                    assertEquals(aTile, testSniperBee.getPosition(), "Constructor did not instantiate the field position correctly");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }
    }



    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:insect-takedamage-basic-1")
    
    @DisplayName("Test Insect takeDamage(): update hp")
    void testTakeDamage1() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile t = new Tile();
                t.buildHive();
                BusyBee.BASE_HEALTH = 5;
                Insect bee = new BusyBee(t);
                HoneyBee.HIVE_DMG_REDUCTION = 10.0 / 100.0;
                bee.takeDamage(2);
                assertEquals(4, bee.getHealth(), "health points are not updated correctly");
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:insect-takedamage-basic-2")
    
    @DisplayName("Test Insect takeDamage(): remove insect")
    void testTakeDamage2() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile t = new Tile();
                t.buildHive();
                BusyBee.BASE_HEALTH = 5;
                Insect bee = new BusyBee(t);
                bee.takeDamage(6);
                assertEquals(null, bee.getPosition(), "insect is not removed correctly");

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:honeybee-getcost-1")
    
    @DisplayName("Test HoneyBee getCost()")
    void testHoneyBeeGetCost() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                Tile t = new Tile();
                BusyBee.BASE_COST = 2;
                HoneyBee bee = new BusyBee(t);
                assertEquals(2, bee.getCost(), "Cost should be 2");

            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:honeybee-takedamage-1")
    @DisplayName("Test HoneyBee takeDamage()")
    public void testTakeDamageOnHive() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile tile = new Tile();
                tile.buildHive();
                BusyBee.BASE_HEALTH = 5;
                BusyBee.BASE_COST = 2;

                HoneyBee bee = new BusyBee(tile);
                tile.addInsect(bee);
                int damage = 10;
                int expectedHealth = bee.getHealth() - ((int) (damage - damage * HoneyBee.HIVE_DMG_REDUCTION));
                bee.takeDamage(damage);
                assertEquals(expectedHealth, bee.getHealth(), "takeDamage() not overridden correctly for bees on hive");
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:hornet-takeaction")
    
    @DisplayName("Test takeAction(): stings")
    void testHornetTakeAction1() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                try {
                    String[] fieldNames = getPathFieldsNames();
                    String onPath = fieldNames[0];
                    Tile t = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(t, true);

                    BusyBee.BASE_HEALTH = 5;
                    HoneyBee bee = new BusyBee(t);
                    Hornet hornet = new Hornet(t, 10, 2);
                    hornet.takeAction();

                    assertEquals(3, bee.getHealth(), "Hornet: takeAction() does the wrong amount of damage to bee. Expected: 3, actual: " + bee.getHealth());
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Tag("score:1")
    @Tag("P")
    @Tag("token:hornet-queen-1")
    
    @DisplayName("Test isTheQueen")
    void testIsTheQueenHornetQueen() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                String[] names = getPathFieldsNames();
                assertNotNull(names);
                for (String s : names)
                    assertNotNull(s);

                String onPath = names[0];

                String[] queenNames = getQueenFieldsNames();
                assertNotNull(queenNames);
                for (String s : queenNames)
                    assertNotNull(s);

                String queen = queenNames[0];
                try {
                    Tile aTile = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(aTile, true);

                    Hornet queenH = new Hornet(aTile, 4, 1);
                    Field queenField = Hornet.class.getDeclaredField(queen);

                    queenField.setAccessible(true);
                    queenField.set(queenH, true);
                    assertEquals(queenH.isTheQueen(), true, "isTheQueen() does not retrieve right queen value, hornet is queen but not shown to be");
                    queenField.set(queenH, false);
                    assertEquals(queenH.isTheQueen(), false, "isTheQueen() does not retrieve right queen value, hornet is not queen but shown to be");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }
    }

    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:testBusyBeeTakeAction")
    
    @DisplayName("Test BusyBee takeAction()")
    void testBusyBeeTakeAction() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile t = new Tile();
                BusyBee.BASE_AMOUNT_COLLECTED = 2;
                BusyBee bee = new BusyBee(t);

                assertTrue(bee.takeAction(), "takeAction() returns the wrong value. Expected: true, actual: false");
                int food = t.collectFood();
                assertEquals(2, food, "takeAction() adds wrong amount of food to the tile. Expected: 2, actual:" + food);
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("P")
    @Tag("token:angrybee-action-false-1")
    @DisplayName("Test AngryBee takeAction(): bee not on the path")
    public void testTakeActionReturnsFalse() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile t = new Tile();
                AngryBee bee = new AngryBee(t, 2);
                assertFalse(bee.takeAction(), "takeAction() returns the wrong value.");
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:takeaction")
    
    @DisplayName("Test AngryBee takeAction(): angry bee stings")
    void testTakeActionDamagesHornet() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                Tile hive = new Tile();
                hive.buildHive();
                Tile nest = new Tile();
                nest.buildNest();
                Tile t = new Tile();
                t.createPath(hive, nest); // create path

                AngryBee bee = new AngryBee(t, 2); // attack = 2
                Hornet hornet = new Hornet(t, 10, 2);

                t.addInsect(bee);
                t.addInsect(hornet);

                assertTrue(bee.takeAction(), "takeAction() returns the wrong value.");
                assertEquals(8, hornet.getHealth(), "takeAction() does the wrong amount of damage to hornet.");
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:takeaction")
    @DisplayName("Test AngryBee takeAction(): hornet on the next tile towards the nest")
    public void testTakeActionHornetsOnNextTile() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                try {
                    Tile hive = new Tile();
                    hive.buildHive();
                    Tile nest = new Tile();
                    nest.buildNest();
                    Tile tile1 = new Tile();
                    Tile tile2 = new Tile();
                    tile2.createPath(hive, nest); // create path
                    tile1.createPath(hive, nest); // create path

                    String[] fieldNames = getPathFieldsNames();

                    String towardTheNestFieldName = fieldNames[2];
                    Field towardTheNestField = Tile.class.getDeclaredField(towardTheNestFieldName);
                    towardTheNestField.setAccessible(true);
                    towardTheNestField.set(tile1, tile2);

                    AngryBee bee = new AngryBee(tile1, 2);
                    tile1.addInsect(bee);
                    tile2.addInsect(new Hornet(tile2, 10, 2));
                    assertTrue(bee.takeAction(), "Bee should sting hornet on the next tile");
                } catch (NoSuchFieldException e) {
                    // Handle the exception here
                    fail("The field towardTheNest does not exist in the Tile class.");
                } catch (ReflectiveOperationException e) {
                    // Handle other reflective operation exceptions
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("M")
    @Tag("token:targetFireBee")
    
    @DisplayName("Test no target tile within range")
    void testNoTileWithinTargetFireBee() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                String[] names = getPathFieldsNames();
                assertNotNull(names);
                for (String s : names)
                    assertNotNull(s);

                String onPath = names[0];
                String towHive = names[1];
                String towNest = names[2];

                try {
                    // Path currentTile -> previousTile1 -> previousTile2 -> previousTile3
                    Tile previousTile1 = new Tile();
                    Tile previousTile2 = new Tile();
                    Tile previousTile3 = new Tile();
                    Tile currentTile = new Tile();

                    Field towardNestField = Tile.class.getDeclaredField(towNest);
                    towardNestField.setAccessible(true);
                    towardNestField.set(previousTile2, previousTile3);
                    towardNestField.set(previousTile1, previousTile2);
                    towardNestField.set(currentTile, previousTile1);

                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(currentTile, true);
                    onPathField.set(previousTile1, true);
                    onPathField.set(previousTile2, true);
                    onPathField.set(previousTile3, true);

                    previousTile1.setOnFire();
                    //previousTile2.setOnFire();

                    Hornet aHornet = new Hornet(previousTile3, 2, 2);

                    FireBee testFirebee = new FireBee(currentTile, 2);
                    boolean actionTaken = testFirebee.takeAction();
                    assertFalse(actionTaken, "takeAction() should not run since no target within range but did");
                    assertTrue(previousTile1.isOnFire(), "fired tile should stay on fire");
                    assertFalse(previousTile2.isOnFire(), "the second tile should not be on fire");
                    assertFalse(previousTile3.isOnFire(), "non-fired outside range tile should not be on fire");
                } catch (Exception e) {
                    fail("Test failed with an exception: " + e.getMessage());
                    //e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }

    @Test
    @Tag("score:1")
    @Tag("M")
    @Tag("token:targetFireBee")
    
    @DisplayName("Test no attacks on nest or the tile with the bee itself")
    void testNoAttacksOnNestOrTileItselfFireBee() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                String[] names = getPathFieldsNames();
                assertNotNull(names);
                for (String s : names)
                    assertNotNull(s);

                String onPath = names[0];
                String towHive = names[1];
                String towNest = names[2];
                try {
                    Tile previousTile = new Tile();
                    Tile currentTile = new Tile();
                    Field towardNestField = Tile.class.getDeclaredField(towNest);
                    towardNestField.setAccessible(true);
                    towardNestField.set(currentTile, previousTile);
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(currentTile, true);
                    onPathField.set(previousTile, true);

                    previousTile.buildNest();

                    FireBee testFirebee = new FireBee(currentTile, 1);
                    boolean actionTaken = testFirebee.takeAction();
                    assertFalse(actionTaken, "takeAction() should not run since no attack on nest or tile itself");
                    assertFalse(previousTile.isOnFire(), "tile with nest should not be on fire");
                    assertFalse(currentTile.isOnFire(), "tile with bee should not be on fire");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }
    }


    @Tag("score:1")
    @Tag("AM")
    @Tag("token:testAddHornet")
    
    @DisplayName("Test addHornet adds a Hornet to the swarm")
    void testAddHornet() {
        try {
            String[] fieldNames = getPathFieldsNames();
            String onPath = fieldNames[0];
            Tile t = new Tile();
            Field onPathField = Tile.class.getDeclaredField(onPath);
            onPathField.setAccessible(true);
            onPathField.set(t, true);

            Hornet hornet = new Hornet(t, 10, 2);
            SwarmOfHornets swarm = new SwarmOfHornets();
            swarm.addHornet(hornet);

            assertEquals(1, swarm.sizeOfSwarm());

            Hornet[] hornets = swarm.getHornets();
            assertNotNull(hornets, "Hornets array should not be null");
            assertTrue(Arrays.asList(hornets).contains(hornet),
                    "Hornet should appear in swarm array after being added");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:testAddHornet")
    
    @DisplayName("Test that the addHornet method adds the hornet to the swarm by adding two hornets and checking the size of the swarm")
    void testAddHornetToSwarm1() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                try {
                    String[] fieldNames = getPathFieldsNames();
                    String onPath = fieldNames[0];
                    Tile t = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(t, true);

                    Hornet hornet1 = new Hornet(t, 10, 2);
                    Hornet hornet2 = new Hornet(t, 20, 2);
                    SwarmOfHornets swarm = new SwarmOfHornets();
                    swarm.addHornet(hornet1);
                    assertEquals(1, swarm.sizeOfSwarm());
                    swarm.addHornet(hornet2);
                    assertEquals(2, swarm.sizeOfSwarm());

                    Hornet[] hornets = swarm.getHornets();
                    assertNotNull(hornets, "Hornets array should not be null");
                    assertTrue(Arrays.asList(hornets).contains(hornet1),
                            "Hornet1 should appear in swarm array after being added");
                    assertTrue(Arrays.asList(hornets).contains(hornet2),
                            "Hornet2 should appear in swarm array after being added");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }


    @Test
    @Tag("score:1")
    @Tag("AM")
    @Tag("token:testGetHornetsFromSwarm")
    
    @DisplayName("Test SwarmOfHornets.getHornets() removing the middle hornet updates array order to [first, third]")
    void testGetHornetsFromSwarm() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                // TEST HERE
                try {
                    String[] fieldNames = getPathFieldsNames();
                    String onPath = fieldNames[0];
                    Tile t = new Tile();
                    Field onPathField = Tile.class.getDeclaredField(onPath);
                    onPathField.setAccessible(true);
                    onPathField.set(t, true);

                    Hornet hornet1 = new Hornet(t, 10, 2);
                    Hornet hornet2 = new Hornet(t, 20, 2);
                    Hornet hornet3 = new Hornet(t, 30, 2);
                    SwarmOfHornets swarm = new SwarmOfHornets();
                    swarm.addHornet(hornet1);
                    swarm.addHornet(hornet2);
                    swarm.addHornet(hornet3);

                    // Initial array: [hornet1, hornet2, hornet3]
                    Hornet[] hornets = swarm.getHornets();
                    assertNotNull(hornets);
                    assertEquals(3, hornets.length);

                    // Remove the MIDDLE hornet (hornet2)
                    swarm.removeHornet(hornet2);

                    // After removal, array should be [hornet1, hornet3] in this order
                    hornets = swarm.getHornets();
                    assertNotNull(hornets);
                    assertEquals(2, hornets.length);
                    assertSame(hornet1, hornets[0], "First element should remain hornet1 after removing the middle hornet");
                    assertSame(hornet3, hornets[1], "Second element should be hornet3 after removing the middle hornet");
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            });

            // Set a timeout for the thread execution (e.g., 10 seconds)
            future.get(250, MILLISECONDS);
        } catch (TimeoutException e) {
            // Thread execution took longer than the specified timeout
            fail("Test timed out");
        } catch (Exception e) {
            // Handle other exceptions
            fail("Test failed with an exception: " + e.getMessage());
        } finally {
            executor.shutdownNow(); // Interrupt the thread
        }

    }
}