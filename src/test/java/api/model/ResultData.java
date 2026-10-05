package api.model;
import java.util.Map;

public class ResultData {

    /** Сообщение сервера о результате операции. */
    private String message;

    /** Произвольные данные результата, например {@code {"id": 42}}. */
    private Map<String, Long> data;

    /** Пустой конструктор — обязателен для Jackson. */
    public ResultData() {
    }

    // =================================================================
    //  Бизнес-хелпер: «достань id созданного товара»
    // =================================================================

    /**
     * Достаёт идентификатор товара из карты данных результата.
     *
     * @return значение по ключу {@code id}, либо {@code null}, если ключа нет
     */
    public Long getDataId() {
        return data == null ? null : data.get("id");
    }

    // =================================================================
    //  Аксессоры (getters / setters)
    // =================================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Long> getData() {
        return data;
    }

    public void setData(Map<String, Long> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultData{message='" + message + "', data=" + data + '}';
    }
}