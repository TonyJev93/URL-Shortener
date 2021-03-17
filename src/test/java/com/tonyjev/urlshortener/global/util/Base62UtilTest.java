package com.tonyjev.urlshortener.global.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BASE62 인코딩 Utill 테스트")
class Base62UtilTest {
    public static final String testDvision = "=============";

    @BeforeAll
    static void beforeAll() {
        System.out.println(testDvision + " Before All : 테스트 시작 " + testDvision);
    }

    @AfterAll
    static void afterAll() {
        System.out.println(testDvision + " After All : 테스트 종료 " + testDvision);
    }

    @Test
    @DisplayName("Encoding 테스트")
    public void encodingTest() {
        assertAll(
                () -> assertEquals("1", Base62Util.encoding(1)),
                () -> assertEquals("9", Base62Util.encoding(9)),

                () -> assertEquals("a", Base62Util.encoding(10)),
                () -> assertEquals("z", Base62Util.encoding(35)),

                () -> assertEquals("A", Base62Util.encoding(36)),
                () -> assertEquals("Z", Base62Util.encoding(61)),

                () -> {
                    long maxEncodableNumber = (long) Math.pow(62, 8) - 1L;  // 인코딩 결과 8자리 테스트 (= 62의 8제곱 - 1)
                    assertEquals("ZZZZZZZZ", Base62Util.encoding(maxEncodableNumber));
                }
        );

        System.out.println("Encoding 테스트 통과");
    }


    @Test
    @DisplayName("Decoding 테스트")
    public void decodingTest() {

        assertAll(
                () -> assertEquals(1, Base62Util.decoding("1")),
                () -> assertEquals(9, Base62Util.decoding("9")),

                () -> assertEquals(10, Base62Util.decoding("a")),
                () -> assertEquals(35, Base62Util.decoding("z")),

                () -> assertEquals(36, Base62Util.decoding("A")),
                () -> assertEquals(61, Base62Util.decoding("Z")),

                () -> {
                    long maxEncodableNumber = (long) Math.pow(62, 8) - 1L; // 인코딩 결과 8자리 테스트 (= 62의 8제곱 - 1)
                    assertEquals(maxEncodableNumber, Base62Util.decoding("ZZZZZZZZ"));
                }
        );

        System.out.println("Decoding 통과");
    }
}
