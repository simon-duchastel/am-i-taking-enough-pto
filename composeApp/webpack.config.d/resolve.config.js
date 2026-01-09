// Webpack 5 no longer includes Node.js core module polyfills by default
// This configuration adds fallbacks for modules that may be required
config.resolve = config.resolve || {};
config.resolve.fallback = {
    ...config.resolve.fallback,
    fs: false,
    path: false,
    crypto: false,
    os: false,
    buffer: false,
    stream: false,
    util: false,
    assert: false,
    http: false,
    https: false,
    url: false,
    zlib: false
};
