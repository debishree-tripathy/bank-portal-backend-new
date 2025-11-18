import React, { useEffect, useState } from "react";
import axios from "axios";

export default function UserLanding({ fullName, userId, role, onLogout }) {
  const [balance, setBalance] = useState(null);
  const [recentTransactions, setRecentTransactions] = useState([]);

  // Fetch balance and recent transactions on component mount
  useEffect(() => {
    if (!userId) return;

    const fetchBalance = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/transactions/balance/${userId}`
        );
        setBalance(response.data.balance);
      } catch (err) {
        console.error("Error fetching balance:", err);
        setBalance("Error fetching balance");
      }
    };

    const fetchTransactions = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/transactions/recent/${userId}`
        );
        setRecentTransactions(response.data.transactions || []);
      } catch (err) {
        console.error("Error fetching transactions:", err);
        setRecentTransactions([]);
      }
    };

    fetchBalance();
    fetchTransactions();
  }, [userId]);

  const formatCurrency = (amount) =>
    new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR" }).format(amount);

  const formatDate = (dateString) =>
    new Date(dateString).toLocaleString("en-IN", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });

  return (
    <div className="dashboard-container">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-logo">
          <img src="/logo.png" alt="Bank Logo" className="bank-logo" />
        </div>
        <nav className="sidebar-nav">
          <button>Accounts</button>
          <button>Transactions</button>
          <button>Cards</button>
          <button>Investments</button>
          <button>Profile</button>
          <button>Support</button>
        </nav>
        <div style={{ flexGrow: 1 }}></div>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </aside>

      {/* Main Content */}
      <main className="dashboard-main">
        <header className="dashboard-header">
          <h2>Welcome, {fullName}!</h2>
        </header>

        <section className="dashboard-cards">
          {/* Current Balance */}
          <div className="card">
            <h3>Current Balance</h3>
            <p style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
              {balance !== null ? formatCurrency(balance) : "Loading..."}
            </p>
          </div>

          {/* Recent Transactions */}
          <div className="card">
            <h3>Recent Transactions</h3>
            {recentTransactions.length === 0 ? (
              <p>No recent transactions</p>
            ) : (
              <table style={{ width: "100%", borderCollapse: "collapse" }}>
                <thead>
                  <tr>
                    <th style={{ textAlign: "left", padding: "8px" }}>Date</th>
                    <th style={{ textAlign: "left", padding: "8px" }}>Type</th>
                    <th style={{ textAlign: "right", padding: "8px" }}>Amount</th>
                  </tr>
                </thead>
                <tbody>
                  {recentTransactions.map((tx) => (
                    <tr key={tx.id} style={{ borderBottom: "1px solid #ccc" }}>
                      <td style={{ padding: "8px" }}>{formatDate(tx.date)}</td>
                      <td style={{ padding: "8px" }}>{tx.type}</td>
                      <td style={{ padding: "8px", textAlign: "right" }}>
                        {formatCurrency(tx.amount)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>

          {/* Quick Actions */}
          <div className="card">
            <h3>Quick Actions</h3>
            <div className="quick-actions">
              <div className="quick-action-card">Transfer Money</div>
              <div className="quick-action-card">Pay Bills</div>
              <div className="quick-action-card">Download Statement</div>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}
