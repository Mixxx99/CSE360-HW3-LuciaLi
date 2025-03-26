package application;

import databasePart1.DatabaseHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseHelperTest {

    private DatabaseHelper databaseHelper;
    private User testUser;
    private User adminUser;
    private Question testQuestion;
    private Answer testAnswer;

    @BeforeAll
    void setup() throws SQLException {
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Setting up test database...");

        databaseHelper = new DatabaseHelper();
        databaseHelper.connectToDatabase();
        databaseHelper.deleteAllUsers();

        testUser = new User("testUser", "password", "user", "test@example.com", "Test User");
        adminUser = new User("adminUser", "adminpass", "admin", "admin@example.com", "Admin");

        databaseHelper.register(testUser);
        databaseHelper.register(adminUser);
        System.out.println("Test users created.");

        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);

        testAnswer = new Answer(questionId, "testUser", "This is a test answer.");
        int answerId = databaseHelper.addAnswer(testAnswer);
        testAnswer.setAnswerId(answerId);
        System.out.println("New Answer ID: " + answerId);
    }


    /**
     * Test 1: Add and Retrieve Questions.
     * Verifies that a question can be added by a user and later retrieved
     * from the database using getAllQuestions().
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Add and Retrieve Questions")
    void testAddAndRetrieveQuestions() throws SQLException {
        System.out.println(" ");
        System.out.println("Test1");
        Question newQuestion = new Question("testUser", "Another Question", "Another test content.");
        int questionId = databaseHelper.addNewQuestion(newQuestion);
        System.out.println("New Question ID: " + questionId);

        List<Question> questions = databaseHelper.getAllQuestions();
        assertTrue(questions.stream().anyMatch(q -> q.getQuestionId() == questionId));
        System.out.println("Test 1: Succeed!");
    }

    /**
     * Test 2.1: Edit Question (User).
     * Checks if a normal user can update the title and content of their own question.
     * Uses updateQuestion() and confirms with getAllQuestions().
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Edit Question user")
    void testEditQuestion() throws SQLException {
        System.out.println(" ");
        System.out.println("Test2.1");
        
        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);

        testAnswer = new Answer(questionId, "testUser", "This is a test answer.");
        int answerId = databaseHelper.addAnswer(testAnswer);
        testAnswer.setAnswerId(answerId);
        System.out.println("New Answer ID: " + answerId);

        boolean success = databaseHelper.updateQuestion(testQuestion.getQuestionId(), "Updated Title", "Updated Content", testUser.getUserName());
        System.out.println("Update success: " + success);
        assertTrue(success);

        List<Question> questions = databaseHelper.getAllQuestions();
        assertTrue(questions.stream().anyMatch(q -> q.getTitle().equals("Updated Title")));
        System.out.println("Test 2.1: Succeed!");
    }
    
    /**
     * Test 2.2: Edit Question (Admin).
     * Checks if an admin can update another user’s question.
     * Verifies that admin privileges allow editing any question.
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Edit Question admin")
    void testEditQuestion2() throws SQLException {
        System.out.println(" ");
        System.out.println("Test2.2");
        
        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);

        testAnswer = new Answer(questionId, "testUser", "This is a test answer.");
        int answerId = databaseHelper.addAnswer(testAnswer);
        testAnswer.setAnswerId(answerId);
        System.out.println("New Answer ID: " + answerId);

        boolean success = databaseHelper.updateQuestion(testQuestion.getQuestionId(), "Updated Title", "Updated Content", adminUser.getUserName());
        System.out.println("Update success: " + success);
        assertTrue(success);

        List<Question> questions = databaseHelper.getAllQuestions();
        assertTrue(questions.stream().anyMatch(q -> q.getTitle().equals("Updated Title")));
        System.out.println("Test 2.2: Succeed!");
    }

    
    /**
     * Test 3: Add and Retrieve Answers.
     * Adds an answer to a question and checks if it appears in getAnswersForQuestion().
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Add and Retrieve Answers")
    void testAddAndRetrieveAnswers() throws SQLException {
        System.out.println(" ");
        System.out.println("Test3");
        Answer newAnswer = new Answer(testQuestion.getQuestionId(), "testUser", "Another test answer.");
        int answerId = databaseHelper.addAnswer(newAnswer);
        System.out.println("New Answer ID: " + answerId);
        assertTrue(answerId > 0);

        List<Answer> answers = databaseHelper.getAnswersForQuestion(testQuestion.getQuestionId());
        assertTrue(answers.stream().anyMatch(a -> a.getAnswerId() == answerId));
        System.out.println("Test 3: Succeed!");
    }

    
    /**
     * Test 4.1: Edit Answer (User).
     * Checks if a user can update their own answer.
     * Validates that the updated answer is stored correctly.
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Edit Answer user")
    void testEditAnswer1() throws SQLException {
        System.out.println(" ");
        System.out.println("Test4.1");
        
      
        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);

        testAnswer = new Answer(questionId, "testUser", "This is a test answer.");
        int answerId = databaseHelper.addAnswer(testAnswer);
        testAnswer.setAnswerId(answerId);
        System.out.println("New Answer ID: " + answerId);

        boolean success = databaseHelper.updateAnswer(testAnswer.getAnswerId(), "Updated Answer", testUser);
        System.out.println("Answer Update success: " + success);
        assertTrue(success);

        List<Answer> answers = databaseHelper.getAnswersForQuestion(testAnswer.getQuestionId());
        assertTrue(answers.stream().anyMatch(a -> a.getAnswerContent().equals("Updated Answer")));
        System.out.println("Test 4.1: Succeed!");
    }
    
    
    /**
     * Test 4.2: Edit Answer (Admin).
     * Ensures that an admin can edit any answer.
     * Verifies that the change is correctly reflected in the database.
     *
     * @throws SQLException if a database error occurs
     */
    @Test
    @DisplayName("Edit Answer admin")
    void testEditAnswer2() throws SQLException {
        System.out.println(" ");
        System.out.println("Test4.2");
        
        
        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);

        testAnswer = new Answer(questionId, "testUser", "This is a test answer.");
        int answerId = databaseHelper.addAnswer(testAnswer);
        testAnswer.setAnswerId(answerId);
        System.out.println("New Answer ID: " + answerId);
        
        boolean success = databaseHelper.updateAnswer(testAnswer.getAnswerId(), "Updated Answer", adminUser);
        System.out.println("Answer Update success: " + success);
        assertTrue(success);

        List<Answer> answers = databaseHelper.getAnswersForQuestion(testAnswer.getQuestionId());
        assertTrue(answers.stream().anyMatch(a -> a.getAnswerContent().equals("Updated Answer")));
        System.out.println("Test 4.2: Succeed!");
    }

    @Test
    @DisplayName("User Disconnects Answer")
    void testUserDisconnectsAnswer() throws SQLException {
        System.out.println(" ");
        System.out.println("Test5.1");

        databaseHelper.disconnectAnswerFromUser(testAnswer.getAnswerId(), testUser.getUserName());
        System.out.println("Answer ID " + testAnswer.getAnswerId() + " disconnected from user.");

        List<Answer> answers = databaseHelper.getAnswersForQuestion(testAnswer.getQuestionId());
        assertTrue(answers.stream().anyMatch(a -> a.getUserName().equals("unknown")));
        System.out.println("Test 5.1: Succeed!");
    }

    @Test
    @DisplayName("Admin Deletes Answer Permanently")
    void testAdminDeletesAnswer() throws SQLException {
        System.out.println(" ");
        System.out.println("Test5.2");

        Answer tempAnswer = new Answer(testQuestion.getQuestionId(), "testUser", "Temporary Answer");
        int tempAnswerId = databaseHelper.addAnswer(tempAnswer);
        System.out.println("Temporary Answer ID: " + tempAnswerId);

        databaseHelper.deleteAnswerPermanently(tempAnswerId);
        System.out.println("Temporary Answer ID " + tempAnswerId + " deleted.");

        List<Answer> answers = databaseHelper.getAnswersForQuestion(testQuestion.getQuestionId());
        assertFalse(answers.stream().anyMatch(a -> a.getAnswerId() == tempAnswerId));
        System.out.println("Test 5.2: Succeed!");
    }
    
    @Test
    @DisplayName("User Disconnects Question")
    void testUserDisconnectsQuestion() throws SQLException {
        System.out.println(" ");
        System.out.println("Test6.1");


        testQuestion = new Question("testUser", "Test Question Title", "This is a test question.");
        int questionId = databaseHelper.addNewQuestion(testQuestion);
        testQuestion.setQuestionId(questionId);
        System.out.println("New Question ID: " + questionId);
        
        databaseHelper.disconnectQuestionFromUser(testQuestion.getQuestionId(), testUser.getUserName());
        System.out.println("Question ID " + testQuestion.getQuestionId() + " disconnected from user.");

        List<Question> questions = databaseHelper.getAllQuestions();
        assertTrue(questions.stream().anyMatch(q -> q.getQuestionId() == testQuestion.getQuestionId() && q.getUserName().equals("unknown")));
        System.out.println("Test 6.1: Succeed!");
    }

    @Test
    @DisplayName("Admin Deletes Question Permanently")
    void testAdminDeletesQuestion() throws SQLException {
        System.out.println(" ");
        System.out.println("Test6.2");

        databaseHelper.deleteQuestionPermanently(testQuestion.getQuestionId());
        System.out.println("Question ID " + testQuestion.getQuestionId() + " deleted.");

        List<Question> questions = databaseHelper.getAllQuestions();
        assertFalse(questions.stream().anyMatch(q -> q.getQuestionId() == testQuestion.getQuestionId()));
        System.out.println("Test 6.2: Succeed!");
    }


    @AfterAll
    void cleanup() throws SQLException {
        System.out.println(" ");
        System.out.println("Cleaning up database...");
        databaseHelper.closeConnection();
        System.out.println("Database closed.");
    }

}
