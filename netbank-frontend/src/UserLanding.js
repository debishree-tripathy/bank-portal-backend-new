import React, { useEffect, useState } from "react";
import axios from "axios";

export default function UserLanding({ fullName, userId, role, onLogout }) {
  const [balance, setBalance] = useState(0);
  const [recentTransactions, setRecentTransactions] = useState([]);
  const [showAllTransactions, setShowAllTransactions] = useState(false);

  // Fetch balance and transactions
  useEffect(() => {
    if (!userId) return;

    const fetchBalance = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/transactions/balance/${userId}`
        );
        setBalance(response.data?.balance ?? 0);
      } catch (err) {
        console.error("Error fetching balance:", err);
        setBalance(0);
      }
    };

    const fetchTransactions = async () => {
      try {
        const endpoint = showAllTransactions
          ? `/api/transactions/all/${userId}`
          : `/api/transactions/recent/${userId}`;

        const response = await axios.get(`http://localhost:8080${endpoint}`);

        const data = Array.isArray(response.data?.transactions)
          ? response.data.transactions
          : [];

        setRecentTransactions(data);
      } catch (err) {
        console.error("Error fetching transactions:", err);
        setRecentTransactions([]);
      }
    };

    fetchBalance();
    fetchTransactions();
  }, [userId, showAllTransactions]);

  const formatCurrency = (amount) =>
    new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: "INR",
    }).format(amount);

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

      {/* Main dashboard */}
      <main className="dashboard-main">
        <header className="dashboard-header">
          <h2>Welcome, {fullName}!</h2>
        </header>

        <section className="dashboard-cards">
          {/* Current Balance */}
          <div className="card">
            <h3>Current Balance</h3>
            <p style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
              {formatCurrency(balance)}
            </p>
          </div>

          {/* Recent / All Transactions */}
          <div className="card">
            <h3>Transactions</h3>
            {recentTransactions.length === 0 ? (
              <p>No transactions found</p>
            ) : (
              <>
                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                  <thead>
                    <tr>
                      <th style={{ textAlign: "left", padding: "8px" }}>Date</th>
                      <th style={{ textAlign: "left", padding: "8px" }}>Type</th>
                      <th style={{ textAlign: "right", padding: "8px" }}>
                        Amount
                      </th>
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

                {recentTransactions.length >= 5 && (
                  <button
                    onClick={() =>
                      setShowAllTransactions(!showAllTransactions)
                    }
                    style={{
                      marginTop: "12px",
                      padding: "8px 16px",
                      borderRadius: "6px",
                      border: "1px solid #333",
                      backgroundColor: "#007bff",
                      color: "#fff",
                      fontWeight: "bold",
                      cursor: "pointer",
                    }}
                  >
                    {showAllTransactions
                      ? "View Less Transactions"
                      : "View All Transactions"}
                  </button>
                )}
              </>
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
