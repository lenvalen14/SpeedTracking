"use client";

import React from "react";
import MapComponent from "@/components/map-container";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { FcGoogle } from "react-icons/fc";

export default function LoginPage() {
  return (
    <div className="flex min-h-screen bg-black">
      {/* Left side - Login Form */}
      <div className="flex flex-col justify-center w-full md:w-1/2 p-8 z-10">
        <div className="max-w-md mx-auto w-full space-y-8">
          <div className="text-white space-y-2">
            <h2 className="text-its-blue text-xl font-medium text-[var(--its-blue)]">Sign in to</h2>
            <h1 className="text-4xl font-bold">SpeedTrack</h1>
          </div>
          
          <Button 
            variant="outline" 
            className="w-full  hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-300 rounded shadow flex items-center justify-center gap-2 h-12 bg-[var(--its-yellow)]" 
          >
            <FcGoogle className="h-5 w-5" />
            <span>Continue with Google</span>
          </Button>
          
          <div className="relative my-6">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-700"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-black text-gray-400">or</span>
            </div>
          </div>
          
          <div className="space-y-4">
            <div>
              <Input
                type="email"
                placeholder="Email"
                className="h-12 bg-gray-900 border-gray-700 text-white"
              />
            </div>
            <div className="relative">
              <Input
                type="password"
                placeholder="Password"
                className="h-12 bg-gray-900 border-gray-700 text-white"
              />
              <button className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-white text-sm">
                Forgot
              </button>
            </div>
            <Button className="w-full h-12 text-white bg-[var(--its-blue)] hover:bg-blue-300">
            Log in
            </Button>

          </div>
        </div>
      </div>

      {/* Right side - Map */}
      <div className="hidden md:block w-1/2 relative p-4">
        <MapComponent />
      </div>
    </div>
  );
}
