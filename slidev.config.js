// Slidev Configuration for Java Skills Project
// This configuration enables all the installed plugins and sets up optimal defaults

export default {
  // Theme configuration - choose one of the installed themes
  theme: 'apple-basic', // Clean, professional theme perfect for educational content
  // Alternative themes: 'geist', 'seriph' (default), 'theme-academic' (when compatible)
  
  // Syntax highlighting is now configured in setup/shiki.ts
  
  // Plugin configuration
  addons: [
    'slidev-addon-java-runner',        // Execute Java code directly in slides
    'slidev-addon-components',         // Enhanced components for interactivity
    'slidev-addon-fancy-arrow',        // Professional arrow annotations
    'slidev-addon-tldraw',            // Hand-drawn style diagrams
    'slidev-addon-d2-diagram',        // Text-to-diagram conversion
    '@katzumi/slidev-addon-qrcode'    // QR codes for resources (can be enabled later)
  ],
  
  // Enhanced configuration options
  remoteAssets: true,
  monaco: {
    theme: 'vs-light',
    fontSize: 16,
    lineNumbers: 'on',
    minimap: { enabled: false },
    scrollBeyondLastLine: false,
    wordWrap: 'on'
  },
  
  // PDF export settings (built-in)
  export: {
    format: 'pdf',
    timeout: 30000,
    dark: false,
    withClicks: true,
    withToc: true
  },
  
  // Drawing and diagram settings
  drawings: {
    enabled: true,
    persist: false,
    presenterOnly: false,
    syncAll: true
  },
  
  // Java-specific configuration
  java: {
    version: '21',
    enablePreview: true,
    classpath: './build/classes/java/main',
    sourceCompat: '21',
    targetCompat: '21'
  },
  
  // Color scheme for consistent branding
  themeConfig: {
    primary: '#00D4FF',      // Our signature blue for highlighting
    secondary: '#333333',    // Dark text
    accent: '#FF6B6B',       // Error/warning highlights
    background: '#FFFFFF',   // Clean white background
    code: '#F8F8F2'         // Code block background
  }
};