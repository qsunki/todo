import { useState, useMemo } from 'react';
import { ActiveFilter } from '../types/todo';
import dayjs from 'dayjs';

interface FilterType {
  label: string;
  options: { label: string; value: string; }[];
}

interface AvailableFilterTypes {
  [key: string]: FilterType;
}

export function useFilters() {
  const [activeFilters, setActiveFilters] = useState<ActiveFilter[]>([]);
  const [selectedFilterTypes, setSelectedFilterTypes] = useState<string[]>([]);

  const availableFilterTypes: AvailableFilterTypes = useMemo(() => ({
    priority: {
      label: '우선순위',
      options: [
        { label: '높음', value: '상' },
        { label: '중간', value: '중' },
        { label: '낮음', value: '하' }
      ]
    },
    // deadline: {
    //   label: '마감일',
    //   options: [
    //     { label: '오늘', value: dayjs().format('YYYY-MM-DDTHH:mm:ss') },
    //     { label: '이번주', value: dayjs().endOf('week').format('YYYY-MM-DDTHH:mm:ss') },
    //     { label: '이번달', value: dayjs().endOf('month').format('YYYY-MM-DDTHH:mm:ss') }
    //   ]
    // }
  }), []);

  const remainingFilterTypes = useMemo(() => 
    Object.entries(availableFilterTypes)
      .filter(([key]) => !selectedFilterTypes.includes(key))
      .map(([key, value]) => ({
        key,
        label: value.label,
      })),
    [availableFilterTypes, selectedFilterTypes]
  );

  const handleAddFilter = (filterType: string) => {
    setSelectedFilterTypes(prev => [...prev, filterType]);
  };

  const handleRemoveFilter = (filterType: string) => {
    setSelectedFilterTypes(prev => prev.filter(type => type !== filterType));
    setActiveFilters(prev => prev.filter(filter => filter.type !== filterType));
  };

  const handleFilterChange = (filterType: string, value: string) => {
    setActiveFilters(prev => {
      const newFilters = prev.filter(f => f.type !== filterType);
      return value ? [...newFilters, { type: filterType, value }] : newFilters;
    });
  };

  return {
    activeFilters,
    selectedFilterTypes,
    availableFilterTypes,
    remainingFilterTypes,
    handleAddFilter,
    handleRemoveFilter,
    handleFilterChange
  };
} 