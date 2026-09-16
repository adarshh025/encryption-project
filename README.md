# Encryption System

<div align="center">
  <img src="assets/logo.png" width="160" alt="Encryption System Logo"/>
  <br/>
  <p><strong>Educational Desktop File Encryption & Decryption Utility</strong></p>
  <p>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License: MIT"/></a>
    <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-8%2B-orange.svg" alt="Java: 8+"/></a>
    <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Build-Maven-red.svg" alt="Build: Maven"/></a>
    <a href="https://github.com/formdev/flatlaf"><img src="https://img.shields.io/badge/UI-FlatLaf%203.4-107C41.svg" alt="UI: FlatLaf"/></a>
  </p>
</div>

An educational desktop file encryption and decryption application developed in Java. The project provides a modern graphical user interface (GUI) to transform `.txt` text files and `.java` source code files into obfuscated ciphertext and back using a byte-level shift cipher.

Created by **Adarsh Aher** as a practical learning project during a Network Security course to explore fundamental concepts of cryptography, byte manipulation, and modern Java Swing GUI development.

---

## Screenshots

| Modern Light Interface | File Loaded & Ready |
| :---: | :---: |
| ![Light Mode Overview](assets/screenshots/app_overview.png) | ![File Selected](assets/screenshots/file_selected.png) |

<div align="center">
  <h3>Dark Mode Theme</h3>
  <img src="assets/screenshots/app_dark_mode.png" width="720" alt="Dark Mode Interface"/>
</div>

---

## Overview

Encryption System demonstrates how reversible data transformation functions at the byte level. It provides:
- **Modern Desktop UI**: Built with FlatLaf supporting high-DPI scaling, smooth rounded cards, and instant Light/Dark mode toggling.
- **Drag-and-Drop Workflow**: Drop target files directly into the application window or browse via native file choosers.
- **Real-Time Key Preview**: Interactive digit-sum feedback updates automatically as you type numeric passwords.
- **Streaming Architecture**: Low memory footprint using buffered I/O streams; handles large files seamlessly.
- **Reversible Shift Cipher**: Byte-level Caesar transformation based on digit-sum key derivation.

---

## Features

- **Modern FlatLaf GUI**: Clean card-based layout with rounded corners, refined typography, and instant dark/light mode toggle.
- **Drag-and-Drop Target**: Drag `.txt` or `.java` files straight from Windows Explorer or macOS Finder into the app.
- **File Metadata Card**: Displays file name, size, format badge (`TEXT`, `JAVA`), and path upon selection.
- **Interactive Key Derivation**: Visual indicator previews the calculated shift key in real-time (e.g., `12345` $\rightarrow$ shift `15`).
- **Safe File Saving**: Automatically handles extensions (`.enc`, `.txt`, `.java`) and prompts before overwriting existing files.
- **Robust Error Handling**: Validates user inputs, reports I/O exceptions gracefully, and prevents application crashes.

---

## Technology Stack

- **Language**: Java (JDK 8 or higher, tested on OpenJDK 17)
- **GUI Framework**: Java Swing with [FlatLaf](https://www.formdev.com/flatlaf/) modern look-and-feel
- **Build Tool**: Apache Maven
- **Test Framework**: JUnit 5 Jupiter (`org.junit.jupiter`)

---

## Architecture & Data Flow

```mermaid
flowchart TD
    A[User Drops / Selects File] --> B[File Validation]
    B --> C[User Enters Numeric Key]
    C --> D[KeyDerivation: Digit-Sum Calculation]
    D --> E{Operation}
    E -->|Encrypt| F[ShiftCipherEngine: byte - key]
    E -->|Decrypt| G[ShiftCipherEngine: byte + key]
    F --> H[Buffered Stream Output]
    G --> H
    H --> I[Encrypted / Decrypted File Saved]
```

### Component Breakdown
- `io.github.adarshh025.encryptionsystem.Main`: Application entry point; initializes FlatLaf modern theme and launches UI.
- `io.github.adarshh025.encryptionsystem.core.CipherEngine`: Contract defining streaming and in-memory encryption/decryption.
- `io.github.adarshh025.encryptionsystem.core.ShiftCipherEngine`: Core streaming byte-shift transformation engine.
- `io.github.adarshh025.encryptionsystem.core.KeyDerivation`: Input validation and base-10 digit-sum key calculation.
- `io.github.adarshh025.encryptionsystem.ui.CryptographyFrame`: Modern Swing window handling drag-and-drop, theme switching, dialogs, and progress states.
- `io.github.adarshh025.encryptionsystem.ui.FileFilters`: File selection filters for text and Java files.

---

## Cryptographic Mechanics

### 1. Key Derivation
When the user enters a numeric password (e.g., `12345`), the system extracts the absolute numeric value and computes the sum of its decimal digits:

$$\text{shift} = \sum_{i} d_i$$

For example, for key `12345`:
$$1 + 2 + 3 + 4 + 5 = 15$$

### 2. Byte Transformation
The cipher operates as a byte-level Caesar/additive cipher:
- **Encryption**: Each byte $b$ is shifted backwards:
  $$c_i = (b_i - \text{shift}) \pmod{256}$$
- **Decryption**: Each ciphertext byte $c$ is restored by adding the shift:
  $$b_i = (c_i + \text{shift}) \pmod{256}$$

---

## Security Considerations

> [!WARNING]
> **Educational Grade Cryptography**:
> This project is designed exclusively for educational and learning purposes. It is **not** intended to protect sensitive, confidential, or production data.

### Known Limitations
1. **Small Key Space**: Since the cipher operates modulo 256, there are at most 256 distinct byte shift states. A brute-force attack can test all possible keys in milliseconds.
2. **Frequency Analysis**: Single-byte additive substitution preserves frequency distributions and byte patterns of the underlying file format.
3. **No Authenticated Encryption**: The format does not include an HMAC or AEAD tag; tampered ciphertext will decrypt into corrupted bytes without raising an authentication error.
4. **Known Plaintext Vulnerability**: Knowledge of standard file headers (e.g. `public class`, `import java.`) immediately discloses the exact key.

---

## Installation & Setup

### Prerequisites
- **Java Development Kit (JDK)**: Version 8, 11, 17, or 21 installed.
- **Apache Maven**: Version 3.6 or higher.

Verify installations:
```bash
java -version
mvn -version
```

### Build from Source
Clone the repository and compile using Maven:
```bash
git clone https://github.com/adarshh025/encryption-project.git
cd encryption-project
mvn clean package
```

This compiles all classes, runs the automated test suite, and packages a standalone executable JAR into `target/encryption-system-1.0.0.jar`.

---

## Usage

Run the packaged application:
```bash
java -jar target/encryption-system-1.0.0.jar
```

Alternatively, run directly with Maven:
```bash
mvn compile exec:java -Dexec.mainClass="io.github.adarshh025.encryptionsystem.Main"
```

### Command-Line Options
```bash
java -jar target/encryption-system-1.0.0.jar --help
java -jar target/encryption-system-1.0.0.jar --version
```

---

## Running Tests

Execute the automated JUnit 5 test suite:
```bash
mvn test
```

The test suite verifies:
- Key derivation edge cases (multi-digit sums, negative numbers, whitespace, zero/null/invalid inputs).
- ASCII and UTF-8 round-trip encryption/decryption.
- Java source code preservation.
- Binary data and full byte range (0x00 to 0xFF).
- Streaming throughput on large files.
- Exact byte-shift verification.
- Incorrect key behavior.

---

## Project Structure

```
encryption-project/
├── .gitignore                          # Git ignore rules for Maven, IDEs, and OS files
├── LICENSE                             # MIT License
├── README.md                           # Project documentation
├── pom.xml                             # Maven project configuration
├── assets/
│   ├── logo.png                        # Brand logo emblem
│   └── screenshots/                    # High-resolution screenshots
│       ├── app_overview.png
│       ├── file_selected.png
│       └── app_dark_mode.png
└── src/
    ├── main/
    │   ├── java/
    │   │   └── io/github/adarshh025/encryptionsystem/
    │   │       ├── Main.java           # Application entry point
    │   │       ├── core/
    │   │       │   ├── CipherEngine.java       # Interface
    │   │       │   ├── KeyDerivation.java      # Key parser & digit sum
    │   │       │   └── ShiftCipherEngine.java  # Streaming Caesar cipher
    │   │       └── ui/
    │   │           ├── CryptographyFrame.java  # Modern FlatLaf GUI
    │   │           └── FileFilters.java        # JFileChooser filters
    │   └── resources/
    │       └── io/github/adarshh025/encryptionsystem/
    │           └── logo.png            # Application icon / brand asset
    └── test/
        └── java/
            └── io/github/adarshh025/encryptionsystem/
                ├── CaptureScreenshots.java     # Screenshot capture utility
                ├── KeyDerivationTest.java      # Key derivation tests
                └── ShiftCipherEngineTest.java  # Cipher roundtrip & edge tests
```

---

## Roadmap & Future Improvements

- [ ] Add modern authenticated encryption mode (AES-256-GCM with PBKDF2 key derivation).
- [ ] Add CLI mode for headless terminal encryption (`encrypt -f input.txt -k 12345 -o output.enc`).
- [ ] Support folder/batch file encryption.

---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## Author

**Adarsh Aher**  
- GitHub: [@adarshh025](https://github.com/adarshh025)
