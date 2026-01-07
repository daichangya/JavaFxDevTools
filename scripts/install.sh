#!/bin/bash
# JavaFxDevTools Installation Script for Linux/macOS
# Usage: ./install.sh [version]

set -e

# Configuration
REPO_URL="https://github.com/daichangya/JavaFxDevTools"
INSTALL_DIR="${HOME}/.local/share/javafxdevtools"
BIN_DIR="${HOME}/.local/bin"
VERSION="${1:-latest}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}JavaFxDevTools Installation Script${NC}"
echo "=================================="
echo ""

# Check Java version
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed. Please install JDK 17 or higher.${NC}"
    echo "Visit: https://adoptium.net/"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}Error: Java 17 or higher is required. Found Java $JAVA_VERSION${NC}"
    exit 1
fi

echo -e "${GREEN}✓${NC} Java version check passed: $(java -version 2>&1 | head -n 1)"
echo ""

# Determine version
if [ "$VERSION" = "latest" ]; then
    echo "Fetching latest version..."
    VERSION=$(curl -s https://api.github.com/repos/daichangya/JavaFxDevTools/releases/latest | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/' | sed 's/v//')
    echo "Latest version: $VERSION"
fi

# Create installation directory
echo "Creating installation directory..."
mkdir -p "$INSTALL_DIR"
mkdir -p "$BIN_DIR"

# Download JAR files
echo ""
echo "Downloading DevTools..."
if curl -L -f -o "$INSTALL_DIR/DevTools-$VERSION.jar" "$REPO_URL/releases/download/v$VERSION/DevTools-$VERSION.jar"; then
    echo -e "${GREEN}✓${NC} DevTools downloaded successfully"
else
    echo -e "${RED}✗${NC} Failed to download DevTools"
    exit 1
fi

echo "Downloading JavaFxEditor..."
if curl -L -f -o "$INSTALL_DIR/JavaFxEditor-$VERSION.jar" "$REPO_URL/releases/download/v$VERSION/JavaFxEditor-$VERSION.jar"; then
    echo -e "${GREEN}✓${NC} JavaFxEditor downloaded successfully"
else
    echo -e "${RED}✗${NC} Failed to download JavaFxEditor"
    exit 1
fi

# Verify checksums (if available)
if curl -L -f -o "$INSTALL_DIR/DevTools-$VERSION.jar.sha256" "$REPO_URL/releases/download/v$VERSION/DevTools-$VERSION.jar.sha256" 2>/dev/null; then
    echo "Verifying DevTools checksum..."
    cd "$INSTALL_DIR"
    if sha256sum -c "DevTools-$VERSION.jar.sha256" 2>/dev/null; then
        echo -e "${GREEN}✓${NC} DevTools checksum verified"
    else
        echo -e "${YELLOW}⚠${NC} DevTools checksum verification failed (continuing anyway)"
    fi
    cd - > /dev/null
fi

# Create launcher scripts
echo ""
echo "Creating launcher scripts..."

cat > "$BIN_DIR/devtools" << EOF
#!/bin/bash
java -jar "$INSTALL_DIR/DevTools-$VERSION.jar" "\$@"
EOF

cat > "$BIN_DIR/javafxeditor" << EOF
#!/bin/bash
java -jar "$INSTALL_DIR/JavaFxEditor-$VERSION.jar" "\$@"
EOF

chmod +x "$BIN_DIR/devtools"
chmod +x "$BIN_DIR/javafxeditor"

echo -e "${GREEN}✓${NC} Launcher scripts created"

# Check if bin directory is in PATH
if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
    echo ""
    echo -e "${YELLOW}⚠${NC} $BIN_DIR is not in your PATH"
    echo ""
    echo "To add it permanently, add this line to your shell profile (~/.bashrc, ~/.zshrc, etc.):"
    echo -e "${GREEN}export PATH=\"\\\$PATH:$BIN_DIR\"${NC}"
    echo ""
    echo "Or run this command now:"
    echo -e "${GREEN}export PATH=\"\\\$PATH:$BIN_DIR\"${NC}"
else
    echo -e "${GREEN}✓${NC} $BIN_DIR is already in your PATH"
fi

echo ""
echo -e "${GREEN}Installation completed!${NC}"
echo ""
echo "Applications installed to: $INSTALL_DIR"
echo "Launcher scripts created in: $BIN_DIR"
echo ""
echo "You can now run:"
echo -e "  ${GREEN}devtools${NC}"
echo -e "  ${GREEN}javafxeditor${NC}"

