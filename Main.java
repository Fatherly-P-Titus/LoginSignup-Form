
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Main extends Application {
    
    private Crypter crypter;
    private Validator validator;
    private TabPane tabPane;
    private TextField signupUsername, signupEmail, loginUsername;
    private PasswordField signupPassword, signupPassphrase, loginPassword, loginPassphrase;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize crypto and validator
        this.crypter = new Crypter(); // You'll need to implement this
        this.validator = new Validator(crypter);
        
        // Create main tab pane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Create tabs
        Tab signupTab = createSignupTab();
        Tab loginTab = createLoginTab();
        
        tabPane.getTabs().addAll(signupTab, loginTab);
        
        // Create main scene
        Scene scene = new Scene(tabPane, 500, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        primaryStage.setTitle("School Manager - Login/Signup");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private Tab createSignupTab() {
        Tab tab = new Tab("Sign Up");
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = new Label("Create New Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Form fields
        VBox formLayout = new VBox(15);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setMaxWidth(350);
        
        // Username field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username:");
        signupUsername = new TextField();
        signupUsername.setPromptText("Enter your username");
        signupUsername.setPrefHeight(35);
        usernameBox.getChildren().addAll(usernameLabel, signupUsername);
        
        // Email field
        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email:");
        signupEmail = new TextField();
        signupEmail.setPromptText("Enter your email");
        signupEmail.setPrefHeight(35);
        emailBox.getChildren().addAll(emailLabel, signupEmail);
        
        // Password field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password:");
        signupPassword = new PasswordField();
        signupPassword.setPromptText("Enter your password");
        signupPassword.setPrefHeight(35);
        passwordBox.getChildren().addAll(passwordLabel, signupPassword);
        
        // Passphrase field
        VBox passphraseBox = new VBox(5);
        Label passphraseLabel = new Label("Passphrase:");
        signupPassphrase = new PasswordField();
        signupPassphrase.setPromptText("Enter your passphrase");
        signupPassphrase.setPrefHeight(35);
        passphraseBox.getChildren().addAll(passphraseLabel, signupPassphrase);
        
        // Sign up button
        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        signupButton.setPrefHeight(40);
        signupButton.setPrefWidth(200);
        
        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");
        
        // Add all components to form
        formLayout.getChildren().addAll(
            usernameBox, emailBox, passwordBox, passphraseBox, 
            signupButton, statusLabel
        );
        
        mainLayout.getChildren().addAll(titleLabel, formLayout);
        
        // Sign up button action
        signupButton.setOnAction(e -> handleSignup(statusLabel));
        
        tab.setContent(mainLayout);
        return tab;
    }
    
    private Tab createLoginTab() {
        Tab tab = new Tab("Login");
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = new Label("Login to Your Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Form fields
        VBox formLayout = new VBox(15);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setMaxWidth(350);
        
        // Username field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username:");
        loginUsername = new TextField();
        loginUsername.setPromptText("Enter your username");
        loginUsername.setPrefHeight(35);
        usernameBox.getChildren().addAll(usernameLabel, loginUsername);
        
        // Password field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password:");
        loginPassword = new PasswordField();
        loginPassword.setPromptText("Enter your password");
        loginPassword.setPrefHeight(35);
        passwordBox.getChildren().addAll(passwordLabel, loginPassword);
        
        // Passphrase field
        VBox passphraseBox = new VBox(5);
        Label passphraseLabel = new Label("Passphrase:");
        loginPassphrase = new PasswordField();
        loginPassphrase.setPromptText("Enter your passphrase");
        loginPassphrase.setPrefHeight(35);
        passphraseBox.getChildren().addAll(passphraseLabel, loginPassphrase);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(200);
        
        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");
        
        // Add all components to form
        formLayout.getChildren().addAll(
            usernameBox, passwordBox, passphraseBox, 
            loginButton, statusLabel
        );
        
        mainLayout.getChildren().addAll(titleLabel, formLayout);
        
        // Login button action
        loginButton.setOnAction(e -> handleLogin(statusLabel));
        
        tab.setContent(mainLayout);
        return tab;
    }
    
    private void handleSignup(Label statusLabel) {
        // Get form data
        String username = signupUsername.getText().trim();
        String email = signupEmail.getText().trim();
        String password = signupPassword.getText();
        String passphrase = signupPassphrase.getText();
        
        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passphrase.isEmpty()) {
            showError(statusLabel, "All fields are required!");
            return;
        }
        
        // Validate email format
        String emailError = Validator.validateEmail(email);
        if (emailError != null) {
            showError(statusLabel, emailError);
            return;
        }
        
        // Validate password strength
        String passwordError = Validator.validatePassword(password);
        if (passwordError != null) {
            showError(statusLabel, passwordError);
            return;
        }
        
        // Validate username (basic check)
        if (username.length() < 3) {
            showError(statusLabel, "Username must be at least 3 characters long");
            return;
        }
        
        // Here you would typically:
        // 1. Generate a unique ID for the user
        // 2. Encrypt the password and passphrase
        // 3. Save user data to file using your storage mechanism
        // 4. Assign appropriate permissions
        
        try {
            // Generate a unique 7-character ID
            String userId = generateUserId();
            
            // Prepare user data for storage
            UserData newUser = new UserData(userId, username, email, password, passphrase, "USER");
            
            // Save user credentials (you'll need to implement this based on your file structure)
            boolean saveSuccess = saveUserData(newUser);
            
            if (saveSuccess) {
                showSuccess(statusLabel, "Account created successfully! User ID: " + userId);
                clearSignupForm();
                
                // Switch to login tab after successful signup
                tabPane.getSelectionModel().select(1);
            } else {
                showError(statusLabel, "Failed to create account. Please try again.");
            }
            
        } catch (Exception e) {
            showError(statusLabel, "Error during signup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleLogin(Label statusLabel) {
        // Get form data
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();
        String passphrase = loginPassphrase.getText();
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty() || passphrase.isEmpty()) {
            showError(statusLabel, "All fields are required!");
            return;
        }
        
        try {
            // Validate credentials using your Validator class
            boolean isValid = validator.validateAdminUserCredentials(username, password);
            
            if (isValid) {
                showSuccess(statusLabel, "Login successful! Welcome back, " + username);
                clearLoginForm();
                
                // Here you would typically:
                // 1. Load the main application dashboard
                // 2. Set up user session with appropriate permissions
                // 3. Navigate to the main application interface
                
            } else {
                showError(statusLabel, "Invalid username, password, or passphrase!");
            }
            
        } catch (Exception e) {
            showError(statusLabel, "Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean saveUserData(UserData userData) {
        // Implement your user data saving logic here
        // This should save to your users.txt file in the encrypted format
        // You'll need to integrate with your existing file structure
        
        try {
            // Example implementation - adjust based on your actual file format
            return validator.saveUserCredential(userData.getUserId(), userData.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String generateUserId() {
        // Generate a 7-character alphanumeric ID
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            id.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return id.toString();
    }
    
    private void showError(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.RED);
    }
    
    private void showSuccess(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.GREEN);
    }
    
    private void clearSignupForm() {
        signupUsername.clear();
        signupEmail.clear();
        signupPassword.clear();
        signupPassphrase.clear();
    }
    
    private void clearLoginForm() {
        loginUsername.clear();
        loginPassword.clear();
        loginPassphrase.clear();
    }
    
    // Helper class to store user data
    private static class UserData {
        private String userId;
        private String username;
        private String email;
        private String password;
        private String passphrase;
        private String userType;
        
        public UserData(String userId, String username, String email, String password, String passphrase, String userType) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.password = password;
            this.passphrase = passphrase;
            this.userType = userType;
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getPassphrase() { return passphrase; }
        public String getUserType() { return userType; }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}