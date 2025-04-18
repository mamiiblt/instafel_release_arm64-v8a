import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  webpack(config) {
    config.resolve.fallback = {
      ...config.resolve.fallback,

      fs: false,
    };
    return config;
  },
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "raw.githubusercontent.com",
        pathname: "/**",
      },
    ],
  },
  env: {
    NEXT_PUBLIC_SITE_URL: "https://instafel.mamiiblt.me",
  },
  redirects: async () => {
    return [
      {
        source: "/home",
        destination: "/",
        permanent: true,
      },
      {
        source: "/guide",
        destination: "/guides",
        permanent: true,
      },
    ];
  },
};

export default nextConfig;
