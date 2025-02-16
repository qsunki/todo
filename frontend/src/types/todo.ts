export interface TodoProperty {
  id: number;
  name: string;
  type: 'DATE' | 'SELECT';
  data: DateData | SelectData;
}

export interface DateData {
  type: 'date';
  start: string;
  end: string | null;
}

export interface SelectData {
  type: 'select';
  name: string;
  color: string;
}

export interface Todo {
  id: number;
  content: string;
  completed: boolean;
  createdTime: string;
  lastModifiedTime: string;
  properties: TodoProperty[];
}

export interface FilterOption {
  label: string;
  value: string;
}

export interface FilterType {
  label: string;
  options: FilterOption[];
}

export interface FilterTypes {
  [key: string]: FilterType;
}

export interface ActiveFilter {
  type: string;
  value: string;
}

export interface TodoFilter {
  name: string;
  type: 'SELECT' | 'DATE';
  data: PropertyData;
}

export interface PropertyData {
  type: 'select' | 'date';
  name?: string;
  color?: string;
  start?: string;
  end?: string | null;
} 