# 🏦 BlackRock Self-Saving API – Automated Micro-Investing Solution

This project is a **production-grade RESTful API** developed for the **BlackRock 2026 Hackathon**.  
It addresses the *“retirement gap”* by automating micro-investments through **transaction rounding** and **temporal saving rules**, projecting long-term wealth growth in **NPS** and **Index Funds**.

---

## 🚀 Core Features & Business Logic

### **Step 1: Automated Rounding Engine**
- Scans user expenses and calculates the **“remanent”** needed to reach the next ₹100 multiple.  
- Example: An expense of ₹1,519 results in a ₹1,600 ceiling and a ₹81 remanent.

---

### **Step 2: Temporal Constraints Validation**
- **Rule `q` (Priority Overrides)**: Implements logic where specific fixed-saving rules override default rounding based on the **latest start date** priority.  
- **Rule `p` (Cumulative Enhancements)**: Dynamically sums all overlapping extra-saving rules for a specific transaction date.

---

### **Step 3: Evaluation & Wealth Projection**
- **Grouped Analysis (`k` periods)**: Organizes savings into independent evaluation windows for granular reporting.  
- **Inflation-Adjusted Projections**: Calculates “Real Value” returns at age 60 using a 5.5% inflation rate.  
- **Investment Diversity**: Compares **NPS (7.11% + Tax Benefits)** vs. **Index Funds (14.49%)**.

---

### **Performance Monitoring**
A dedicated health endpoint tracks system performance, including:
- Memory usage (MB)
- Active thread count
- Request throughput

---

## 🛠️ Technical Stack

| Category | Technology |
|-----------|-------------|
| **Language / Framework** | Java 17, Spring Boot 3.x |
| **Data Handling** | Maven, Lombok, Java Streams API |
| **Deployment** | Docker (Amazon Corretto 17 Base) |

---

## 📥 Installation & Setup

### **1. Build the Application**
Run the following command to compile the source code and generate the executable JAR file:

```bash
./mvnw clean package
```

---

### **2. Run via Docker**
The application is configured to run on **port 5477**.

```bash
# Build the Docker image
docker build -t blk-hacking-ind-gauri-barge .

# Start the container
docker run -p 5477:5477 blk-hacking-ind-gauri-barge
```

---

## 📘 API Reference

| Method | Endpoint | Description |
|--------|-----------|-------------|
| **POST** | `/blackrock/challenge/v1/transactions:filter` | Processes temporal rules (`q`, `p`, `k`) for transaction data |
| **POST** | `/blackrock/challenge/v1/returns:calculate` | Calculates inflation-adjusted projections and tax benefits |
| **GET**  | `/blackrock/challenge/v1/performance` | Returns real-time system metrics |

---

## 🧠 Author
**Gauri Barge**  
Hackathon Project – BlackRock 2026
