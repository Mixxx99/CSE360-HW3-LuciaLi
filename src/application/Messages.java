package application;

public class Messages {
    private int messageId;
    private int questionId;
    private String userName;
    private String messageContent;

    // Constructor with messageId
    public Messages(int messageId, int questionId, String userName, String messageContent) {
        this.messageId = messageId;
        this.questionId = questionId;
        this.userName = userName;
        this.messageContent = messageContent;
    }

    // Constructor without messageId (for new messages)
    public Messages(int questionId, String userName, String messageContent) {
        this.messageId = -1;
        this.questionId = questionId;
        this.userName = userName;
        this.messageContent = messageContent;
    }

    // Getters
    public int getMessageId() { return messageId; }
    public int getQuestionId() { return questionId; }
    public String getUserName() { return userName == null ? "unknown" : userName; }
    public String getMessageContent() { return messageContent; }

    // Setters
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

}
