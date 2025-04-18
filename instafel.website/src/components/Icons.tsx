import React from "react";

interface IconProps extends React.SVGProps<SVGSVGElement> {
  size?: number;
  color?: string;
}
const defaultIconProps: IconProps = {
  size: 24,
  color: "currentColor",
  strokeWidth: 2,
  strokeLinecap: "round",
  strokeLinejoin: "round",
};

const createIcon = (path: React.ReactNode) => {
  const IconComponent = ({
    size = 24,
    color = "currentColor",
    ...props
  }: IconProps) => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke={color}
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
      {...defaultIconProps}
      {...props}
    >
      {path}
    </svg>
  );

  IconComponent.displayName = "IconComponent";
  return IconComponent;
};
