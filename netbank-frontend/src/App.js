import React, { useState } from "react";
import "./App.css";
import Signup from "./Signup";
import UserLanding from "./UserLanding";
import axios from "axios";

// ===== LOGIN COMPONENT =====
function Login({ onSwitchToSignup, onLoginSuccess }) {
  const [usernameOrEmail, setUsernameOrEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/users/login", {
        username: usernameOrEmail,
        password,
      });

      const { role, message, error, userId } = response.data;

      if (role) {
        const fullName = message.replace("Welcome, ", "").replace("!", "");
        onLoginSuccess(fullName, role, userId); // send role too
      } else if (error) {
        alert(error);
      } else {
        alert("Invalid username/email or password!");
      }
    } catch (err) {
      alert(err.response?.data?.error || "Error connecting to backend");
    }
  };

  return (
    <div className="login-page-container">
      <header className="bank-header">
        <img src="/logo.png" alt="Bank Logo" className="bank-logo" />
      </header>

      <div className="login-box login-box-full">
        <h2>Sign in to your account</h2>
        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label>Username or Email</label>
            <input
              type="text"
              value={usernameOrEmail}
              onChange={(e) => setUsernameOrEmail(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit">Login</button>
        </form>

        <p className="forgot-password" onClick={() => alert("Forgot Password clicked!")}>
          Forgot Password?
        </p>

        <p style={{ marginTop: 12 }}>
          Don’t have an account?{" "}
          <span className="signup-link" onClick={onSwitchToSignup}>
            Sign up
          </span>
        </p>
      </div>
    </div>
  );
}

// ===== APP COMPONENT =====
function App() {
  const [showSignup, setShowSignup] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loggedInFullName, setLoggedInFullName] = useState("");
  const [loggedInRole, setLoggedInRole] = useState("");
  const [loggedInUserId, setLoggedInUserId] = useState(null);

  const handleLoginSuccess = (fullName, role, userId) => {
    setLoggedInFullName(fullName);
    setLoggedInRole(role);
    setLoggedInUserId(userId);
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setLoggedInFullName("");
    setLoggedInRole("");
    setLoggedInUserId(null);
    setIsLoggedIn(false);
    setShowSignup(false);
  };

  return (
    <div className="App">
      {isLoggedIn ? (
        loggedInRole === "USER" ? (
          <UserLanding
            fullName={loggedInFullName}
            userId={loggedInUserId}
            role={loggedInRole}
            onLogout={handleLogout}
          />
        ) : (
          <div style={{ padding: "50px", textAlign: "center" }}>
            <h2>Admin Dashboard Coming Soon</h2>
            <button onClick={handleLogout} style={{ marginTop: "20px" }}>
              Logout
            </button>
          </div>
        )
      ) : showSignup ? (
        <Signup onSwitchToLogin={() => setShowSignup(false)} />
      ) : (
        <Login
          onSwitchToSignup={() => setShowSignup(true)}
          onLoginSuccess={handleLoginSuccess}
        />
      )}
    </div>
  );
}

export default App;
