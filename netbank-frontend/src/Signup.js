import React, { useState } from "react";
import axios from "axios";

export default function Signup({ onSwitchToLogin }) {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const body = { firstName, lastName, email, phoneNumber, username, password, role: "USER" };
      const response = await axios.post("http://localhost:8080/api/users/register", body);
      if (response.status === 201 || response.status === 200) {
        alert(response.data.message || "Signup successful! Please login.");
        setFirstName(""); setLastName(""); setEmail(""); setPhoneNumber(""); setUsername(""); setPassword("");
        if (onSwitchToLogin) onSwitchToLogin();
      } else {
        alert("Signup response: " + JSON.stringify(response.data));
      }
    } catch (err) {
      alert(err.response?.data?.error || "Error connecting to backend during signup");
    }
  };

  return (
    <div className="page-container" style={{ justifyContent: "flex-start", paddingTop: "50px" }}>
      <header className="bank-header">
        <img src="/logo.png" alt="Bank Logo" className="bank-logo" />
      </header>

      <div className="signup-box">
        <button
          type="button"
          onClick={onSwitchToLogin}
          style={{
            backgroundColor: "#cccccc",
            color: "#333",
            border: "none",
            padding: "8px 16px",
            borderRadius: "6px",
            marginBottom: "15px",
            cursor: "pointer",
            fontWeight: 500,
            width: "100%"
          }}
        >
          ← Go Back
        </button>

        <h2>Create New Account</h2>
        <form onSubmit={handleSignup}>
          <div className="form-group"><label>First Name</label><input type="text" value={firstName} onChange={e => setFirstName(e.target.value)} required /></div>
          <div className="form-group"><label>Last Name</label><input type="text" value={lastName} onChange={e => setLastName(e.target.value)} required /></div>
          <div className="form-group"><label>Email</label><input type="email" value={email} onChange={e => setEmail(e.target.value)} required /></div>
          <div className="form-group"><label>Phone</label><input type="text" value={phoneNumber} onChange={e => setPhoneNumber(e.target.value)} required /></div>
          <div className="form-group"><label>Username</label><input type="text" value={username} onChange={e => setUsername(e.target.value)} required /></div>
          <div className="form-group"><label>Password</label><input type="password" value={password} onChange={e => setPassword(e.target.value)} required /></div>
          <button type="submit">Sign Up</button>
        </form>

        <p style={{ marginTop: 12 }}>
          Already have an account?{" "}
          <span className="signup-link" onClick={onSwitchToLogin}>Login</span>
        </p>
      </div>
    </div>
  );
}
