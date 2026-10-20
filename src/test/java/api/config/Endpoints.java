package api.config;

/**
 * =====================================================================
 *  Endpoints — ЕДИНАЯ точка правды для всех URL и путей REST API
 * =====================================================================
 *
 *  Базовый адрес и порт берутся из {@link Config} (config.properties).
 */
public final class Endpoints {

    // =================================================================
    //  БАЗОВЫЙ URL СЕРВИСА (из config.properties через Config)
    // =================================================================
    private static final String BASE_URI = Config.getBaseUri();
    private static final int PORT = Config.getPort();

    // =================================================================
    //  ПУТИ (паттерны) — без хост/порта, только «хвост» URL
    // =================================================================
    private static final String GOODS = "/goods";

    /** POST /goods/add — добавить новый товар (без параметров пути). */
    public static final String GOODS_ADD = GOODS + "/add";

    /** GET /goods/list — список товаров с пагинацией (параметры page/size). */
    public static final String GOODS_LIST = GOODS + "/list";

    /**
     * GET/PATCH/DELETE /goods/{id} — работа с товаром по идентификатору.
     * Переменная часть пути (id) подставляется методом {@link #goodsById(long)}.
     */
    private static final String GOODS_BY_ID = GOODS + "/{id}";

    // Полный адрес «корня» сервиса: baseUri + порт.
    public static final String BASE_URL = BASE_URI + ":" + PORT;

    /**
     * Полный URL для товара с конкретным идентификатором.
     *
     * @param id идентификатор товара
     * @return строка вида {@code http://127.0.0.1:8080/goods/42}
     */
    public static String goodsById(long id) {
        return BASE_URL + GOODS_BY_ID.replace("{id}", String.valueOf(id));
    }

    /**
     * Полный URL списка товаров с параметрами пагинации.
     *
     * @param page номер страницы (начиная с 0)
     * @param size размер страницы
     * @return строка вида {@code http://127.0.0.1:8080/goods/list?page=0&size=10}
     */
    public static String goodsList(int page, int size) {
        return BASE_URL + GOODS_LIST + "?page=" + page + "&size=" + size;
    }

    /**
     * Базовый URL сервиса — используется для конфигурации RestAssured.
     *
     * @return строка вида {@code http://127.0.0.1:8080}
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Только схема + хост, без порта.
     * Нужен для RestAssured, который принимает baseUri и port раздельно.
     *
     * @return строка вида {@code http://127.0.0.1}
     */
    public static String getBaseUri() {
        return BASE_URI;
    }

    /**
     * Номер порта сервиса.
     *
     * @return целое число порта (по умолчанию 8080)
     */
    public static int getPort() {
        return PORT;
    }

    /**
     * Приватный конструктор — запрещаем создание экземпляров.
     * Класс утилитный: обращаемся только через статику.
     */
    private Endpoints() {
        throw new AssertionError("Утилитный класс не инстанцируется");
    }
}
