# School Manager Login/Signup Application - Technical Documentation

**Developer:** Fatherly P. Titus  
**Date:** December 2024  
**Version:** 1.0

## Table of Contents
1. [Application Overview](#application-overview)
2. [Architecture Design](#architecture-design)
3. [Component Breakdown](#component-breakdown)
4. [Validation System](#validation-system)
5. [Data Flow](#data-flow)
6. [Security Implementation](#security-implementation)
7. [File Structure](#file-structure)
8. [Usage Instructions](#usage-instructions)
9. [Extension Points](#extension-points)

## Application Overview

### Purpose
The School Manager Login/Signup Application is a JavaFX-based desktop application that provides secure user authentication and registration functionality. It serves as the entry point for a larger School Management System, handling user creation, credential validation, and access control.

### Key Features
- **Dual-tab Interface**: Separate Sign Up and Login forms
- **Comprehensive Validation**: Real-time input validation using custom Validator class
- **Secure Storage**: Encrypted credential storage
- **User Management**: Complete user lifecycle from registration to authentication
- **Role-based Access**: Foundation for permission-based system access

## Architecture Design

### System Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    LoginSignupApp (JavaFX)                  │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   SignUp    │  │    Login    │  │     Validator       │  │
│  │    Tab      │  │    Tab      │  │                     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    Business Logic Layer                     │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Crypter   │  │ RepoManager │  │    Permission       │  │
│  │             │  │             │  │                     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     Data Access Layer                       │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────┐  ┌─────────────────────────────┐  │
│  │    users.txt        │  │     designations.txt        │  │
│  │ (Encrypted Data)    │  │   (Permission Mapping)      │  │
│  └─────────────────────┘  └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Design Patterns Used
1. **MVC Pattern**: Separation of UI (View), business logic (Controller), and data (Model)
2. **Singleton Pattern**: Validator and Crypter instances
3. **Factory Pattern**: UserData object creation
4. **Observer Pattern**: JavaFX event handling

## Component Breakdown

### 1. Main Application Class: `LoginSignupApp`

#### Responsibilities
- Application initialization and lifecycle management
- UI component assembly and layout
- Event handling coordination
- Navigation between application sections

#### Key Methods
- `start(Stage primaryStage)`: Application entry point
- `createSignupTab()`: Builds registration interface
- `createLoginTab()`: Builds authentication interface
- `handleSignup(Label statusLabel)`: Processes user registration
- `handleLogin(Label statusLabel)`: Processes user login

### 2. Validator Class Integration

#### Validation Methods Utilized
```java
// Email validation
Validator.validateEmail(email)

// Password strength validation  
Validator.validatePassword(password)

// Credential authentication
validator.validateAdminUserCredentials(username, password)

// User credential storage
validator.saveUserCredential(userId, password)
```

#### Pattern Matching Capabilities
- **Email**: Comprehensive RFC-compliant email validation
- **Password**: Minimum 8 chars, requires letter + number + special characters
- **Nigerian Phone**: Supports all major Nigerian telecom formats
- **Names**: Allows international naming conventions with hyphens and apostrophes

### 3. User Interface Components

#### SignUp Tab Structure
```
VBox (mainLayout)
├── Label (titleLabel)
└── VBox (formLayout)
    ├── VBox (usernameBox)
    │   ├── Label
    │   └── TextField
    ├── VBox (emailBox)
    │   ├── Label  
    │   └── TextField
    ├── VBox (passwordBox)
    │   ├── Label
    │   └── PasswordField
    ├── VBox (passphraseBox)
    │   ├── Label
    │   └── PasswordField
    ├── Button (signupButton)
    └── Label (statusLabel)
```

#### Login Tab Structure
```
VBox (mainLayout)
├── Label (titleLabel)
└── VBox (formLayout)
    ├── VBox (usernameBox)
    │   ├── Label
    │   └── TextField
    ├── VBox (passwordBox)
    │   ├── Label
    │   └── PasswordField
    ├── VBox (passphraseBox)
    │   ├── Label
    │   └── PasswordField
    ├── Button (loginButton)
    └── Label (statusLabel)
```

### 4. Data Model: `UserData` Inner Class

#### Properties
```java
private String userId;      // 7-character alphanumeric ID
private String username;    // User-chosen identifier
private String email;       // Verified email address
private String password;    // Encrypted password
private String passphrase;  // Additional security layer
private String userType;    // Role: "USER", "ADMIN", etc.
```

## Validation System

### Input Validation Flow

#### SignUp Validation Sequence
1. **Presence Check**: All fields required
2. **Email Format**: RFC-compliant email validation
3. **Password Strength**: 
   - Minimum 8 characters
   - At least one letter and one number
   - Allows specific special characters
4. **Username Requirements**: Minimum 3 characters
5. **System Integration**: ID generation and file storage validation

#### Login Validation Sequence
1. **Presence Check**: All fields required
2. **Credential Verification**: 
   - Username/password combination check
   - Passphrase validation
   - User type verification

### Error Handling Strategy

#### User Feedback Levels
1. **Field-level Errors**: Specific input format issues
2. **Form-level Errors**: Cross-field validation failures
3. **System-level Errors**: File I/O, encryption issues
4. **Success Messages**: Operation completion confirmation

## Data Flow

### Registration Process
```
User Input → Field Validation → ID Generation → Data Encryption → File Storage → Success Feedback
     ↓              ↓               ↓              ↓               ↓              ↓
[Form Fields] → [Validator] → [generateUserId()] → [Crypter] → [users.txt] → [Status Label]
```

### Authentication Process  
```
User Input → Field Validation → Credential Decryption → File Lookup → Permission Check → Access Grant
     ↓              ↓                 ↓                 ↓             ↓               ↓
[Form Fields] → [Validator] → [Crypter.decodeDecrypt()] → [users.txt] → [designations.txt] → [Main App]
```

## Security Implementation

### Encryption Integration
- **Crypter Class**: Handles all encryption/decryption operations
- **Data Protection**: User credentials stored in encrypted format
- **Secure Transmission**: In-memory data handling best practices

### Access Control
```java
// Permission-based access checking
validator.hasAccess(userId, "ADMIN_PANEL");
validator.hasWriteAccess(userId, "STUDENT_RECORDS");
validator.hasDeleteAccess(userId, "USER_MANAGEMENT");
```

### Credential Security
- **Password Requirements**: Complex pattern enforcement
- **Passphrase Layer**: Additional authentication factor
- **Session Management**: Secure credential handling in memory

## File Structure

### Project Organization
```
SchoolManager/
├── src/
│   └── SchoolManager/
│       ├── LoginSignupApp.java      # Main application class
│       ├── Validator.java           # Validation & security logic
│       ├── Crypter.java            # Encryption/decryption handling
│       ├── RepoManager.java        # Data repository management
│       ├── Permission.java         # Access control logic
│       └── styles.css              # UI styling
├── data/
│   ├── users.txt                   # Encrypted user credentials
│   └── designations.txt           # User permission mappings
└── docs/
    └── README.md                   # Project documentation
```

### Data Files Specification

#### users.txt Format
```
encrypted_user_id,encrypted_username,encrypted_password,encrypted_user_type
```

#### designations.txt Format  
```
user_id,permission_scope,access_type1,access_type2,...
```

## Usage Instructions

### Installation & Setup
1. **Prerequisites**: Java 8+, JavaFX SDK
2. **Compilation**: `javac --module-path %PATH_TO_FX% --add-modules javafx.controls SchoolManager/*.java`
3. **Execution**: `java --module-path %PATH_TO_FX% --add-modules javafx.controls SchoolManager.LoginSignupApp`

### User Registration
1. Navigate to "Sign Up" tab
2. Complete all required fields:
   - Username (min 3 characters)
   - Valid email address
   - Strong password (8+ chars, letter + number)
   - Secure passphrase
3. Click "Sign Up" button
4. Note generated User ID for future reference

### User Login
1. Navigate to "Login" tab  
2. Enter credentials:
   - Registered username
   - Password
   - Passphrase
3. Click "Login" button
4. Successful authentication grants system access

## Extension Points

### Scalability Considerations

#### Additional User Types
```java
// Extend user type system
public enum UserType {
    STUDENT, TEACHER, ADMIN, PARENT, STAFF
}
```

#### Enhanced Permission System
```java
// Add granular permission controls
public class EnhancedPermission {
    private String userId;
    private Map<String, Set<PermissionType>> scopePermissions;
    private LocalDateTime permissionExpiry;
    private boolean isTemporary;
}
```

#### Database Integration
- Replace file-based storage with SQL database
- Implement connection pooling
- Add database migration scripts

### Internationalization
- Add resource bundles for multi-language support
- Locale-specific validation patterns
- Regional phone number formatting

### Additional Features
- Password reset functionality
- Two-factor authentication
- Session timeout handling
- Audit logging system
- Bulk user import/export

## Technical Specifications

### Performance Characteristics
- **Memory Usage**: Optimized UI component recycling
- **Startup Time**: Lazy loading of non-critical components
- **File I/O**: Buffered operations with exception handling

### Browser Compatibility
- **JavaFX Version**: 8+ compatible
- **Screen Resolution**: Responsive design for 1024x768+
- **Accessibility**: WCAG 2.0 compliant color contrasts

### Security Compliance
- **Data Protection**: Encrypted storage at rest
- **Input Sanitization**: Comprehensive validation pipeline
- **Access Control**: Role-based permission system

---

**Developer Signature:** Fatherly P. Titus  
**Document Version:** 1.0  
**Last Updated:** December 2024
