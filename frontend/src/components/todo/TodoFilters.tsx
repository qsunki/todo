import { Select, Button, Space, Tag, Dropdown, Typography } from 'antd';
import { Plus, X } from 'lucide-react';
import { useFilters } from '../../hooks/useFilters';
import { useEffect } from 'react';
import { TodoFilter } from '../../types/todo';

const { Text } = Typography;

interface TodoFiltersProps {
  onFiltersChange: (filters: TodoFilter[]) => void;
}

function TodoFilters({ onFiltersChange }: TodoFiltersProps) {
  const {
    activeFilters,
    selectedFilterTypes,
    availableFilterTypes,
    remainingFilterTypes,
    handleAddFilter,
    handleRemoveFilter,
    handleFilterChange
  } = useFilters();

  // 필터 변경 시 상위 컴포넌트에 알림
  useEffect(() => {
    const filters = activeFilters.map(filter => {
      const filterType = availableFilterTypes[filter.type];
      const option = filterType.options.find(opt => opt.value === filter.value);
      
      if (!filter.value) return null;  // 빈 값인 경우 필터링

      if (filter.type === 'priority') {
        return {
          name: '우선순위',
          type: 'SELECT',
          data: {
            type: 'select',
            name: filter.value,
            color: null
          }
        };
      }
      
      if (filter.type === 'deadline') {
        return {
          name: '마감일',
          type: 'DATE',
          data: {
            type: 'date',
            start: option?.value,
            end: null
          }
        };
      }

      return null;
    }).filter(Boolean) as TodoFilter[];

    onFiltersChange(filters);
  }, [activeFilters, availableFilterTypes, onFiltersChange]);

  return (
    <div className="space-y-4 mb-6">
      <div>
        <div className="flex justify-between items-center mb-2">
          <Text className="text-gray-500">필터</Text>
          <Dropdown
            menu={{
              items: remainingFilterTypes.map(type => ({
                key: type.key,
                label: type.label,
                onClick: () => handleAddFilter(type.key)
              })),
            }}
            disabled={remainingFilterTypes.length === 0}
            trigger={['click']}
          >
            <Button 
              type="text" 
              size="small"
              icon={<Plus className="w-3 h-3" />}
            >
              필터 추가
            </Button>
          </Dropdown>
        </div>
        <Space direction="vertical" className="w-full">
          {selectedFilterTypes.map((filterType) => (
            <div key={filterType} className="flex gap-2 items-center">
              <Select
                className="flex-1"
                placeholder={availableFilterTypes[filterType].label}
                allowClear
                onChange={(value) => handleFilterChange(filterType, value)}
                options={[
                  { label: '없음', value: '' },
                  ...availableFilterTypes[filterType].options
                ]}
                value={activeFilters.find(f => f.type === filterType)?.value || ''}
              />
              <Button
                type="text"
                icon={<X className="w-4 h-4" />}
                onClick={() => handleRemoveFilter(filterType)}
              />
            </div>
          ))}
        </Space>
        {activeFilters.length > 0 && (
          <div className="mt-2 space-y-1">
            {activeFilters.map((filter, index) => {
              const filterType = availableFilterTypes[filter.type];
              const option = filterType.options.find(opt => opt.value === filter.value);
              return (
                <Tag
                  key={index}
                  closable
                  onClose={() => handleFilterChange(filter.type, '')}
                  className="mr-1 mb-1"
                >
                  {`${filterType.label}: ${option?.label}`}
                </Tag>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

export default TodoFilters; 