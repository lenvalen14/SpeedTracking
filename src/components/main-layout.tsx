"use client";

import React from 'react';
import { Bell, Map, FileText, Shield, PenTool, User, Settings,Siren,LayoutDashboard } from "lucide-react";
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { Tooltip } from 'antd';

function MainLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const pathname = usePathname();

  // Navigation items with their paths
  const navItems = [
    { icon: <LayoutDashboard size={24} />, tooltip: "Dashboard", path: "/" },
    { icon: <Map size={24} />, tooltip: "Maps", path: "/maps" },
    { icon: <Siren size={24} />, tooltip: "Siren", path: "/siren" },
    { icon: <PenTool size={24} />, tooltip: "Design", path: "/design" },
  ];

  const bottomNavItems = [
    { icon: <User size={24} />, tooltip: "Profile", path: "/profile" },
    { icon: <Settings size={24} />, tooltip: "Settings", path: "/settings" },
  ];

  // Check if current route is active
  const isActive = (path: string) => pathname === path;


  return (
    <div className='flex h-screen max-h-screen overflow-hidden'>
      {/* Sidebar Navigation */}
      <div className="m-4 rounded-2xl w-20 bg-[var(--its-blue)] flex flex-col items-center py-4 text-white overflow-y-auto overflow-x-hidden">
        <h2 className="sr-only">Navigation</h2>
        
        {/* Top Navigation Section */}
        <div className="flex flex-col items-center gap-4 flex-shrink-0">
          <Tooltip placement="right" title="Notifications" arrow={{ pointAtCenter: true }}>
            <Link 
              href="/notifications" 
              className={`p-3 rounded-lg ${isActive('/notifications') ? 'bg-white/30' : 'bg-white/0 hover:bg-white/20'} transition-all`}
            >
              <Bell size={24} />
              <span className="sr-only">Notifications</span>
            </Link>
          </Tooltip>
          
          <div className="border-t border-white/30 w-10 mx-auto my-2"></div>
          
          {navItems.map((item, index) => (
            <NavButton 
              key={index}
              icon={item.icon}
              tooltip={item.tooltip}
              path={item.path}
              isActive={isActive(item.path)}
            />
          ))}
        </div>
        
        {/* Bottom Navigation Section */}
        <div className="mt-auto flex flex-col items-center gap-4 pb-4 flex-shrink-0">
          <div className="border-t border-white/30 w-10 mx-auto my-2"></div>
          
          {bottomNavItems.map((item, index) => (
            <NavButton 
              key={index}
              icon={item.icon}
              tooltip={item.tooltip}
              path={item.path}
              isActive={isActive(item.path)}
            />
          ))}
        </div>
      </div>

      {/* Main Content Area */}
      <div className='flex-1 flex flex-col overflow-hidden'>
        <div className="flex-1 overflow-y-auto smooth-scroll">
          {children}
        </div>
      </div>
    </div>
  );
}

interface NavButtonProps {
  icon: React.ReactNode;
  tooltip: string;
  path: string;
  isActive: boolean;
}

const NavButton = ({ icon, tooltip, path, isActive }: NavButtonProps) => {
  return (
    <Tooltip 
      placement="right" 
      title={tooltip} 
      arrow={{ pointAtCenter: true }}
      classNames={{
        root: "antd-tooltip-custom"
      }}
    >
      <Link
        href={path}
        className={`p-3 rounded-lg ${isActive ? 'bg-white/30' : 'bg-white/0 hover:bg-white/20'} transition-all`}
      >
        {icon}
        <span className="sr-only">{tooltip}</span>
      </Link>
    </Tooltip>
  );
};

export default MainLayout;