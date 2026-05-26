import 'zone.js/testing';

declare const require: {
  context(path: string, deep?: boolean, filter?: RegExp): {
    keys(): string[];
    <T>(id: string): T;
  };
};

const context = require.context('./', true, /\.spec\.ts$/);
context.keys().map(context);
