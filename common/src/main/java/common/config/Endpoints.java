package common.config;

public final class Endpoints {
    private static final String GOODS = "/goods";
    public static final String GOODS_ADD = GOODS + "/add";
    public static final String GOODS_LIST = GOODS + "/list";
    private static final String GOODS_BY_ID = GOODS + "/{id}";

    private Endpoints() { throw new AssertionError("Утилитный класс не инстанцируется"); }

    public static String goodsById(long id) {
        return GOODS_BY_ID.replace("{id}", String.valueOf(id));
    }
    public static String getBaseUri() { return Config.getBaseUri(); }
    public static int getPort() { return Config.getPort(); }
    public static String getBaseUrl() { return Config.getBaseUrl(); }
}
