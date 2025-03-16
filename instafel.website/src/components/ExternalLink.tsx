import React from "react";
import { ExternalLink as ExternalLinkIcon } from "./Icons";

interface ExternalLinkProps {
  href: string;
  children: React.ReactNode;
  className?: string;
}

const ExternalLink: React.FC<ExternalLinkProps> = ({
  href,
  children,
  className = "",
}) => {
  // Check if children contains only text (no icon)
  const hasOnlyText = React.Children.toArray(children).every(
    (child) => typeof child === "string",
  );

  return (
    <a
      href={href}
      target="_blank"
      rel="noopener noreferrer"
      className={`inline-flex items-center gap-1 text-primary hover:underline focus:outline-none focus:ring-2 focus:ring-primary ${className}`}
    >
      {children}
      {hasOnlyText && <ExternalLinkIcon />}
    </a>
  );
};

export default ExternalLink;
